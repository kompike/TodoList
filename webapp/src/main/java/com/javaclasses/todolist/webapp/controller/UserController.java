package com.javaclasses.todolist.webapp.controller;

import com.javaclasses.todolist.model.dto.LoginDTO;
import com.javaclasses.todolist.model.dto.RegistrationDTO;
import com.javaclasses.todolist.model.dto.SecurityTokenDTO;
import com.javaclasses.todolist.model.dto.UserDTO;
import com.javaclasses.todolist.model.entity.tinytype.SecurityTokenId;
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

import static com.javaclasses.todolist.webapp.HandlerRegistry.*;
import static com.javaclasses.todolist.webapp.controller.ControllerUtils.*;
import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static javax.servlet.http.HttpServletResponse.SC_OK;

/**
 * Realization of {@link Handler} interface for user management
 */
public class UserController {

    private final Logger log = LoggerFactory.getLogger(UserController.class);

    private final TaskService taskService = TaskServiceImpl.getInstance();
    private final UserService userService = UserServiceImpl.getInstance(taskService);
    private final HandlerRegistry handlerRegistry = HandlerRegistry.getInstance();

    private UserController() {
        registerUser();
        loginUser();
        logoutUser();
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

    private void logoutUser() {
        handlerRegistry.registerHandler(new RequestContext(LOGOUT_URL, POST_METHOD), (request, response) -> {

            final JsonEntity jsonEntity = new JsonEntity();

            final UserDTO user = getUserByToken(request);

            if (user == null) {
                return getUserNotAuthorizedJson(jsonEntity);
            }

            final String requestTokenId = request.getParameter(TOKEN_ID_PARAMETER);

            userService.logout(new SecurityTokenId(Long.valueOf(requestTokenId)));
            jsonEntity.add(MESSAGE_PARAMETER, "User successfully logged out");
            jsonEntity.setResponseStatusCode(SC_OK);

            return jsonEntity;
        });
    }

    public static UserController init() {
        return new UserController();
    }
}
