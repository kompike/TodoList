package com.javaclasses.todolist.webapp.controller;

import com.javaclasses.todolist.model.dto.TaskDTO;
import com.javaclasses.todolist.model.dto.UserDTO;
import com.javaclasses.todolist.model.entity.tinytype.SecurityTokenId;
import com.javaclasses.todolist.model.entity.tinytype.TaskId;
import com.javaclasses.todolist.model.entity.tinytype.UserId;
import com.javaclasses.todolist.model.service.TaskService;
import com.javaclasses.todolist.model.service.UserService;
import com.javaclasses.todolist.model.service.impl.TaskServiceImpl;
import com.javaclasses.todolist.model.service.impl.UserServiceImpl;
import com.javaclasses.todolist.webapp.JsonEntity;

import javax.servlet.http.HttpServletRequest;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

import static com.javaclasses.todolist.webapp.HandlerRegistry.*;
import static javax.servlet.http.HttpServletResponse.SC_FORBIDDEN;

/**
 * Util metods for controllers
 */
/*package*/ final class ControllerUtils {

    private static final TaskService TASK_SERVICE = TaskServiceImpl.getInstance();
    private static final UserService USER_SERVICE = UserServiceImpl.getInstance(TASK_SERVICE);

    private ControllerUtils() {
    }

    /*package*/ static String getUserTaskList(UserId userId) {

        final StringBuilder builder = new StringBuilder("[");
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        final Collection<TaskDTO> allUserTasks = TASK_SERVICE.findAllUserTasks(userId);

        for (TaskDTO taskDTO : allUserTasks) {
            final JsonEntity taskJson = new JsonEntity();
            final TaskId chatId = taskDTO.getTaskId();
            taskJson.add(TASK_ID_PARAMETER, String.valueOf(chatId.getId()));
            taskJson.add(DESCRIPTION_PARAMETER, taskDTO.getDescription());
            taskJson.add(CREATION_DATE_PARAMETER, taskDTO.getCreationDate().format(formatter));
            taskJson.add(STATUS_PARAMETER, String.valueOf(taskDTO.isActive()));
            builder.append(taskJson.generateJson()).append(",");
        }

        if (builder.length() > 1) {
            builder.setLength(builder.length() - 1);
        }
        builder.append("]");

        return builder.toString();
    }


    /*package*/ static UserDTO getUserByToken(HttpServletRequest request) {

        final String requestTokenId = request.getParameter(TOKEN_ID_PARAMETER);
        final SecurityTokenId tokenId = new SecurityTokenId(Long.valueOf(requestTokenId));
        return USER_SERVICE.findByToken(tokenId);
    }

    /*package*/ static JsonEntity getUserNotAuthorizedJson(JsonEntity jsonEntity) {

        jsonEntity.add(ERROR_MESSAGE_PARAMETER, "User not authorized");
        jsonEntity.setResponseStatusCode(SC_FORBIDDEN);

        return jsonEntity;
    }
}
