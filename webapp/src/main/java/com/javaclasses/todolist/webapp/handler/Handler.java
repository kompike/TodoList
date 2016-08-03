package com.javaclasses.todolist.webapp.handler;

import com.javaclasses.todolist.webapp.JsonEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Interface for abstract handler instance
 */
public interface Handler {

    /**
     * Process request data
     * @param request HttpServletRequest from user
     * @param response HttpServletResponse with processed data
     * @return Entity of {@link JsonEntity} with processed data
     */
    JsonEntity process(HttpServletRequest request, HttpServletResponse response);
}
