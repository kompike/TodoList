package com.javaclasses.todolist.webapp.handler;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.javaclasses.todolist.webapp.JsonEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Implementation of {@link Handler} interface for handling error requests
 */
public class PageNotFoundHandler implements Handler {

    @Override
    public JsonEntity process(HttpServletRequest request, HttpServletResponse response) {

        JsonEntity jsonEntity = new JsonEntity();
        final JsonElement jsonElement = new Gson().toJsonTree("error");
        jsonEntity.add("error", jsonElement);
        jsonEntity.setResponseStatusCode(404);
        return jsonEntity;
    }
}
