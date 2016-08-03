package com.javaclasses.todolist.webapp.controller;

import com.javaclasses.todolist.model.dto.RegistrationDTO;
import com.javaclasses.todolist.model.service.UserRegistrationException;
import com.javaclasses.todolist.model.service.UserService;
import com.javaclasses.todolist.model.service.impl.UserServiceImpl;
import com.javaclasses.todolist.webapp.HandlerRegistry;
import com.javaclasses.todolist.webapp.JsonEntity;
import com.javaclasses.todolist.webapp.handler.Handler;
import com.javaclasses.todolist.webapp.handler.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    // Possible request methods
    private static final String POST_METHOD = POST;

    // Request parameter names
    private static final String EMAIL_PARAMETER = "email";
    private static final String PASSWORD_PARAMETER = "password";
    private static final String CONFIRM_PASSWORD_PARAMETER = "confirmPassword";
    private static final String ERROR_MESSAGE_PARAMETER = "errorMessage";
    private static final String MESSAGE_PARAMETER = "message";

    private final UserService userService = UserServiceImpl.getInstance();
    private final HandlerRegistry handlerRegistry = HandlerRegistry.getInstance();

    private UserController() {
        registerUser();
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

    public static UserController init() {
        return new UserController();
    }
}
