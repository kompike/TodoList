package com.javaclasses.todolist.webapp.controller;

import com.javaclasses.todolist.model.dto.AddedTaskDTO;
import com.javaclasses.todolist.model.dto.TaskDTO;
import com.javaclasses.todolist.model.dto.UserDTO;
import com.javaclasses.todolist.model.entity.tinytype.SecurityTokenId;
import com.javaclasses.todolist.model.entity.tinytype.TaskId;
import com.javaclasses.todolist.model.entity.tinytype.UserId;
import com.javaclasses.todolist.model.service.TaskCreationException;
import com.javaclasses.todolist.model.service.TaskService;
import com.javaclasses.todolist.model.service.UserService;
import com.javaclasses.todolist.model.service.impl.TaskServiceImpl;
import com.javaclasses.todolist.model.service.impl.UserServiceImpl;
import com.javaclasses.todolist.webapp.HandlerRegistry;
import com.javaclasses.todolist.webapp.JsonEntity;
import com.javaclasses.todolist.webapp.handler.Handler;
import com.javaclasses.todolist.webapp.handler.RequestContext;

import javax.servlet.http.HttpServletRequest;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

import static javax.servlet.http.HttpServletResponse.*;
import static javax.ws.rs.HttpMethod.POST;

/**
 * Realization of {@link Handler} interface for task management
 */
public class TaskController {

    // Possible request methods
    private static final String POST_METHOD = POST;

    // Request parameter names
    private static final String ERROR_MESSAGE_PARAMETER = "errorMessage";
    private static final String USER_TASKS_PARAMETER = "userTasks";
    private static final String MESSAGE_PARAMETER = "message";
    private static final String TOKEN_ID_PARAMETER = "tokenId";

    private final TaskService taskService = TaskServiceImpl.getInstance();
    private final UserService userService = UserServiceImpl.getInstance(taskService);
    private final HandlerRegistry handlerRegistry = HandlerRegistry.getInstance();

    private TaskController() {
        addNewTask();
        completeTask();
        getAllTasksTask();
        reopenTask();
        deleteTask();
    }

    private void addNewTask() {
        handlerRegistry.registerHandler(new RequestContext("/api/tasks", POST_METHOD), (request, response) -> {

            final JsonEntity jsonEntity = new JsonEntity();

            final UserDTO user = getUserByToken(request);

            if (user == null) {
                return getUserNotAuthorizedJson(jsonEntity);
            }

            final String description = request.getParameter("description");

            try {
                taskService.add(new AddedTaskDTO(description, user.getUserId()));
                final String tasks = getUserTaskList(user.getUserId());

                jsonEntity.add(USER_TASKS_PARAMETER, tasks);
                jsonEntity.add(MESSAGE_PARAMETER, "Task successfully created");
                jsonEntity.setResponseStatusCode(SC_OK);
            } catch (TaskCreationException e) {
                jsonEntity.add(ERROR_MESSAGE_PARAMETER, e.getMessage());
                jsonEntity.setResponseStatusCode(SC_INTERNAL_SERVER_ERROR);
            }

            return jsonEntity;
        });
    }

    private void getAllTasksTask() {
        handlerRegistry.registerHandler(new RequestContext("/api/tasks", "GET"), (request, response) -> {

            final JsonEntity jsonEntity = new JsonEntity();

            final UserDTO user = getUserByToken(request);

            if (user == null) {
                return getUserNotAuthorizedJson(jsonEntity);
            }
            final String tasks = getUserTaskList(user.getUserId());

            jsonEntity.add(USER_TASKS_PARAMETER, tasks);
            jsonEntity.setResponseStatusCode(SC_OK);

            return jsonEntity;
        });
    }

    private void completeTask() {
        handlerRegistry.registerHandler(new RequestContext("/api/tasks/complete", POST_METHOD), (request, response) -> {

            final JsonEntity jsonEntity = new JsonEntity();

            final UserDTO user = getUserByToken(request);

            if (user == null) {
                return getUserNotAuthorizedJson(jsonEntity);
            }

            final String taskId = request.getParameter("taskId");
            taskService.complete(new TaskId(Long.valueOf(taskId)));
            jsonEntity.add("taskId", taskId);
            jsonEntity.add(MESSAGE_PARAMETER, "Task successfully completed");
            jsonEntity.setResponseStatusCode(SC_OK);

            return jsonEntity;
        });
    }

    private void reopenTask() {
        handlerRegistry.registerHandler(new RequestContext("/api/tasks/reopen", POST_METHOD), (request, response) -> {

            final JsonEntity jsonEntity = new JsonEntity();

            final UserDTO user = getUserByToken(request);

            if (user == null) {
                return getUserNotAuthorizedJson(jsonEntity);
            }

            final String taskId = request.getParameter("taskId");
            taskService.reopen(new TaskId(Long.valueOf(taskId)));
            jsonEntity.add("taskId", taskId);
            jsonEntity.add(MESSAGE_PARAMETER, "Task successfully reopened");
            jsonEntity.setResponseStatusCode(SC_OK);

            return jsonEntity;
        });
    }

    private void deleteTask() {
        handlerRegistry.registerHandler(new RequestContext("/api/tasks/delete", POST_METHOD), (request, response) -> {

            final JsonEntity jsonEntity = new JsonEntity();

            final UserDTO user = getUserByToken(request);

            if (user == null) {
                return getUserNotAuthorizedJson(jsonEntity);
            }

            final String taskId = request.getParameter("taskId");
            taskService.delete(new TaskId(Long.valueOf(taskId)));
            jsonEntity.add("taskId", taskId);
            jsonEntity.add(MESSAGE_PARAMETER, "Task successfully deleted");
            jsonEntity.setResponseStatusCode(SC_OK);

            return jsonEntity;
        });
    }


    private UserDTO getUserByToken(HttpServletRequest request) {

        final String requestTokenId = request.getParameter(TOKEN_ID_PARAMETER);
        final SecurityTokenId tokenId = new SecurityTokenId(Long.valueOf(requestTokenId));
        return userService.findByToken(tokenId);
    }

    private JsonEntity getUserNotAuthorizedJson(JsonEntity jsonEntity) {

        jsonEntity.add(ERROR_MESSAGE_PARAMETER, "User not authorized");
        jsonEntity.setResponseStatusCode(SC_FORBIDDEN);

        return jsonEntity;
    }

    private String getUserTaskList(UserId userId) {

        final StringBuilder builder = new StringBuilder("[");
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        final Collection<TaskDTO> allUserTasks = taskService.findAllUserTasks(userId);

        for (TaskDTO taskDTO : allUserTasks) {
            final JsonEntity taskJson = new JsonEntity();
            final TaskId chatId = taskDTO.getTaskId();
            taskJson.add("taskId", String.valueOf(chatId.getId()));
            taskJson.add("description", taskDTO.getDescription());
            taskJson.add("creationDate", taskDTO.getCreationDate().format(formatter));
            taskJson.add("status", String.valueOf(taskDTO.isActive()));
            builder.append(taskJson.generateJson()).append(",");
        }

        if (builder.length() > 1) {
            builder.setLength(builder.length() - 1);
        }
        builder.append("]");

        return builder.toString();
    }
    public static TaskController init() {
        return new TaskController();
    }
}
