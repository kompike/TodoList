package com.javaclasses.todolist.webapp.controller;

import com.javaclasses.todolist.model.dto.LoginDTO;
import com.javaclasses.todolist.model.dto.RegistrationDTO;
import com.javaclasses.todolist.model.dto.SecurityTokenDTO;
import com.javaclasses.todolist.model.dto.TaskDTO;
import com.javaclasses.todolist.model.entity.tinytype.TaskId;
import com.javaclasses.todolist.model.entity.tinytype.UserId;
import com.javaclasses.todolist.model.service.TaskService;
import com.javaclasses.todolist.model.service.UserAuthenticationException;
import com.javaclasses.todolist.model.service.UserRegistrationException;
import com.javaclasses.todolist.model.service.UserService;
import com.javaclasses.todolist.model.service.impl.TaskServiceImpl;
import com.javaclasses.todolist.model.service.impl.UserServiceImpl;
import com.javaclasses.todolist.webapp.HandlerRegistry;
import com.javaclasses.todolist.webapp.JsonEntity;
import com.javaclasses.todolist.webapp.handler.Handler;
import com.javaclasses.todolist.webapp.handler.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.format.DateTimeFormatter;
import java.util.Collection;

import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static javax.servlet.http.HttpServletResponse.SC_OK;
import static javax.ws.rs.HttpMethod.POST;

/**
 * Realization of {@link Handler} interface for user management
 */
public class UserController {

    private final Logger log = LoggerFactory.getLogger(UserController.class);

    // Possible URLs
    private static final String USER_REGISTRATION_URL = "/api/register";
    private static final String LOGIN_URL = "/api/login";

    // Possible request methods
    private static final String POST_METHOD = POST;

    // Request parameter names
    private static final String EMAIL_PARAMETER = "email";
    private static final String PASSWORD_PARAMETER = "password";
    private static final String CONFIRM_PASSWORD_PARAMETER = "confirmPassword";
    private static final String ERROR_MESSAGE_PARAMETER = "errorMessage";
    private static final String USER_TASKS_PARAMETER = "userTasks";
    private static final String MESSAGE_PARAMETER = "message";
    private static final String TOKEN_ID_PARAMETER = "tokenId";

    private final TaskService taskService = TaskServiceImpl.getInstance();
    private final UserService userService = UserServiceImpl.getInstance(taskService);
    private final HandlerRegistry handlerRegistry = HandlerRegistry.getInstance();

    private UserController() {
        registerUser();
        loginUser();
    }

    private void registerUser() {
        handlerRegistry.registerHandler(new RequestContext(USER_REGISTRATION_URL, POST_METHOD), (request, response) -> {

            if (log.isInfoEnabled()) {
                log.info("Start processing registration request...");
            }

            final String email = request.getParameter(EMAIL_PARAMETER);
            final String password = request.getParameter(PASSWORD_PARAMETER);
            final String confirmPassword = request.getParameter(CONFIRM_PASSWORD_PARAMETER);

            final RegistrationDTO registrationDTO =
                    new RegistrationDTO(email, password, confirmPassword);

            final JsonEntity jsonEntity = new JsonEntity();
            try {
                userService.register(registrationDTO);
                jsonEntity.add(MESSAGE_PARAMETER, "User successfully registered");
                jsonEntity.setResponseStatusCode(SC_OK);
            } catch (UserRegistrationException e) {
                jsonEntity.add(ERROR_MESSAGE_PARAMETER, e.getMessage());
                jsonEntity.setResponseStatusCode(SC_INTERNAL_SERVER_ERROR);
            }

            try {
                return jsonEntity;
            } finally {
                if (log.isInfoEnabled()) {
                    log.info("Registration request successfully processed.");
                }
            }
        });
    }

    private void loginUser() {
        handlerRegistry.registerHandler(new RequestContext(LOGIN_URL, POST_METHOD), (request, response) -> {

            if (log.isInfoEnabled()) {
                log.info("Start processing login request...");
            }

            final String email = request.getParameter(EMAIL_PARAMETER);
            final String password = request.getParameter(PASSWORD_PARAMETER);

            final LoginDTO loginDTO =
                    new LoginDTO(email, password);

            final JsonEntity jsonEntity = new JsonEntity();
            try {
                final SecurityTokenDTO tokenDTO = userService.login(loginDTO);
                final String tasks = getUserTaskList(tokenDTO.getUserId());

                jsonEntity.add(TOKEN_ID_PARAMETER, tokenDTO.getTokenId().toString());
                jsonEntity.add(EMAIL_PARAMETER, email);
                jsonEntity.add(USER_TASKS_PARAMETER, tasks);
                jsonEntity.setResponseStatusCode(SC_OK);
            } catch (UserAuthenticationException e) {
                jsonEntity.add(ERROR_MESSAGE_PARAMETER, e.getMessage());
                jsonEntity.setResponseStatusCode(SC_INTERNAL_SERVER_ERROR);
            }

            try {
                return jsonEntity;
            } finally {
                if (log.isInfoEnabled()) {
                    log.info("Login request successfully processed.");
                }
            }
        });
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

    public static UserController init() {
        return new UserController();
    }
}
