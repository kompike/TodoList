package com.javaclasses.todolist.webapp;


import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;

/**
 * Entity for creation and generation of string
 * with necessary data in JSON format
 */
public class JsonEntity {

    private final JsonObject jsonObject = new JsonObject();
    private int responseStatusCode;

    /*package*/ int getResponseStatusCode() {

        if (responseStatusCode == 0) {
            return SC_NOT_FOUND;
        }

        return responseStatusCode;
    }

    public void setResponseStatusCode(int responseStatusCode) {
        this.responseStatusCode = responseStatusCode;
    }

    public void add(String key, Object value){
        final JsonElement jsonElement = new Gson().toJsonTree(value);
        jsonObject.add(key, jsonElement);
    }

    /**
     * Generates string in JSON format
     * @return String in JSON format
     */
    /*package*/ String generateJson() {

        return new Gson().toJson(jsonObject);
    }
}
