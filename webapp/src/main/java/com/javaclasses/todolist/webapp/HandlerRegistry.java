package com.javaclasses.todolist.webapp;

import com.javaclasses.todolist.webapp.handler.Handler;
import com.javaclasses.todolist.webapp.handler.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Registry of all possible handlers
 */
public class HandlerRegistry {

    private final Logger log = LoggerFactory.getLogger(HandlerRegistry.class);

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
    public Handler getHandler(RequestContext requestContext) {

        if (log.isInfoEnabled()) {
            log.info("Start looking for handler..." + requestContext.toString());
        }

        final Handler handler = registry.get(requestContext);

        try {
            return handler;
        } finally {
            if (log.isInfoEnabled()) {
                log.info("Handler successfully found. " + requestContext.toString());
            }
        }
    }
}
