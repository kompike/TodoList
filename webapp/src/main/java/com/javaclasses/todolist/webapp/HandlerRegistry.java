package com.javaclasses.todolist.webapp;

import com.javaclasses.todolist.webapp.handler.Handler;
import com.javaclasses.todolist.webapp.handler.PageNotFoundHandler;
import com.javaclasses.todolist.webapp.handler.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static javax.ws.rs.HttpMethod.GET;
import static javax.ws.rs.HttpMethod.POST;

/**
 * Registry of all possible handlers
 */
public class HandlerRegistry {

    private final Logger log = LoggerFactory.getLogger(HandlerRegistry.class);

    // Possible URLs
    public static final String USER_REGISTRATION_URL = "/api/register";
    public static final String LOGIN_URL = "/api/login";
    public static final String LOGOUT_URL = "/api/logout";
    public static final String TASKS_URL = "/api/tasks";
    public static final String TASK_COMPLETION_URL = "/api/tasks/complete";
    public static final String TASK_REOPENING_URL = "/api/tasks/reopen";
    public static final String TASK_DELETION_URL = "/api/tasks/delete";

    // Possible request methods
    public static final String POST_METHOD = POST;
    public static final String GET_METHOD = GET;

    // Request parameter names
    public static final String EMAIL_PARAMETER = "email";
    public static final String PASSWORD_PARAMETER = "password";
    public static final String CONFIRM_PASSWORD_PARAMETER = "confirmPassword";
    public static final String DESCRIPTION_PARAMETER = "description";
    public static final String TASK_ID_PARAMETER = "taskId";
    public static final String CREATION_DATE_PARAMETER = "creationDate";
    public static final String STATUS_PARAMETER = "status";
    public static final String ERROR_MESSAGE_PARAMETER = "errorMessage";
    public static final String USER_TASKS_PARAMETER = "userTasks";
    public static final String MESSAGE_PARAMETER = "message";
    public static final String TOKEN_ID_PARAMETER = "tokenId";

    private static HandlerRegistry handlerRegistry;

    private HandlerRegistry() {
    }

    public static HandlerRegistry getInstance() {
        if (handlerRegistry == null) {
            handlerRegistry = new HandlerRegistry();
        }

        return handlerRegistry;
    }

    private final Map<RequestContext, Handler> registry = new HashMap<>();

    /**
     * Adds new handler to registry
     * @param context Request data
     * @param handler Handler instance to be executed by given context
     */
    public void registerHandler(RequestContext context, Handler handler) {

        if (log.isInfoEnabled()) {
            log.info("Start adding handler...");
        }

        try {
            registry.put(context, handler);
        } finally {
            if (log.isInfoEnabled()) {
                log.info("New handler successfully added.");
            }
        }
    }

    /**
     * Searches handler by given data
     * @param requestContext Request data
     * @return Handler by given data
     */
    /*package*/ Handler getHandler(RequestContext requestContext) {

        if (log.isInfoEnabled()) {
            log.info("Start looking for handler..." + requestContext.toString());
        }

        final Handler handler = registry.get(requestContext);

        if (handler == null) {
            if (log.isWarnEnabled()) {
                log.warn("Handler by given request context not found: " + requestContext.toString());
            }

            return new PageNotFoundHandler();
        }

        try {
            return handler;
        } finally {
            if (log.isInfoEnabled()) {
                log.info("Handler successfully found. " + requestContext.toString());
            }
        }
    }
}
