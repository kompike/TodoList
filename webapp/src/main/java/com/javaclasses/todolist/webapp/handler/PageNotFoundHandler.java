package com.javaclasses.todolist.webapp.handler;

import com.javaclasses.todolist.webapp.JsonEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Implementation of {@link Handler} interface for handling error requests
 */
public class PageNotFoundHandler implements Handler {

    @Override
    public JsonEntity process(HttpServletRequest request, HttpServletResponse response) {
        return null;
    }
}
