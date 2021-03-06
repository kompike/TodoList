package com.javaclasses.todolist.model.service;

/**
 * Custom exception for catching new task creation failures
 */
public class TaskCreationException extends Exception {

    public TaskCreationException(ErrorMessage message) {
        super(message.toString());
    }
}
