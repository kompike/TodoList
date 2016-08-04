package com.javaclasses.todolist.webapp.controller;

import com.javaclasses.todolist.model.dto.AddedTaskDTO;
import com.javaclasses.todolist.model.dto.UserDTO;
import com.javaclasses.todolist.model.entity.tinytype.TaskId;
import com.javaclasses.todolist.model.service.TaskCreationException;
import com.javaclasses.todolist.model.service.TaskService;
import com.javaclasses.todolist.model.service.impl.TaskServiceImpl;
import com.javaclasses.todolist.webapp.HandlerRegistry;
import com.javaclasses.todolist.webapp.JsonEntity;
import com.javaclasses.todolist.webapp.handler.Handler;
import com.javaclasses.todolist.webapp.handler.RequestContext;

import static com.javaclasses.todolist.webapp.HandlerRegistry.*;
import static com.javaclasses.todolist.webapp.controller.ControllerUtils.*;
import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static javax.servlet.http.HttpServletResponse.SC_OK;

/**
 * Realization of {@link Handler} interface for task management
 */
public class TaskController {

    private final TaskService taskService = TaskServiceImpl.getInstance();
    private final HandlerRegistry handlerRegistry = HandlerRegistry.getInstance();

    private TaskController() {
        addNewTask();
        completeTask();
        getAllTasksTask();
        reopenTask();
        deleteTask();
    }

    private void addNewTask() {
        handlerRegistry.registerHandler(new RequestContext(TASKS_URL, POST_METHOD), (request, response) -> {

            final JsonEntity jsonEntity = new JsonEntity();

            final UserDTO user = getUserByToken(request);

            if (user == null) {
                return getUserNotAuthorizedJson(jsonEntity);
            }

            final String description = request.getParameter(DESCRIPTION_PARAMETER);

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
        handlerRegistry.registerHandler(new RequestContext(TASKS_URL, GET_METHOD), (request, response) -> {

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
        handlerRegistry.registerHandler(new RequestContext(TASK_COMPLETION_URL, POST_METHOD), (request, response) -> {

            final JsonEntity jsonEntity = new JsonEntity();

            final UserDTO user = getUserByToken(request);

            if (user == null) {
                return getUserNotAuthorizedJson(jsonEntity);
            }

            final String taskId = request.getParameter(TASK_ID_PARAMETER);
            taskService.complete(new TaskId(Long.valueOf(taskId)));
            jsonEntity.add(TASK_ID_PARAMETER, taskId);
            jsonEntity.add(MESSAGE_PARAMETER, "Task successfully completed");
            jsonEntity.setResponseStatusCode(SC_OK);

            return jsonEntity;
        });
    }

    private void reopenTask() {
        handlerRegistry.registerHandler(new RequestContext(TASK_REOPENING_URL, POST_METHOD), (request, response) -> {

            final JsonEntity jsonEntity = new JsonEntity();

            final UserDTO user = getUserByToken(request);

            if (user == null) {
                return getUserNotAuthorizedJson(jsonEntity);
            }

            final String taskId = request.getParameter(TASK_ID_PARAMETER);
            taskService.reopen(new TaskId(Long.valueOf(taskId)));
            jsonEntity.add(TASK_ID_PARAMETER, taskId);
            jsonEntity.add(MESSAGE_PARAMETER, "Task successfully reopened");
            jsonEntity.setResponseStatusCode(SC_OK);

            return jsonEntity;
        });
    }

    private void deleteTask() {
        handlerRegistry.registerHandler(new RequestContext(TASK_DELETION_URL, POST_METHOD), (request, response) -> {

            final JsonEntity jsonEntity = new JsonEntity();

            final UserDTO user = getUserByToken(request);

            if (user == null) {
                return getUserNotAuthorizedJson(jsonEntity);
            }

            final String taskId = request.getParameter(TASK_ID_PARAMETER);
            taskService.delete(new TaskId(Long.valueOf(taskId)));
            jsonEntity.add(TASK_ID_PARAMETER, taskId);
            jsonEntity.add(MESSAGE_PARAMETER, "Task successfully deleted");
            jsonEntity.setResponseStatusCode(SC_OK);

            return jsonEntity;
        });
    }

    public static TaskController init() {
        return new TaskController();
    }
}
