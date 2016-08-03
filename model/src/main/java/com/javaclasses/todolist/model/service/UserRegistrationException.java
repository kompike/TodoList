package com.javaclasses.todolist.model.service;

/**
 * Custom exception for catching user registration failures
 */
public class UserRegistrationException extends Exception {

    public UserRegistrationException(String message) {
        super(message);
    }
}
