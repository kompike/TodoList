package com.javaclasses.todolist.model.service;

/**
 * Enum of error messages
 */
public enum ErrorMessage {

    USER_ALREADY_EXISTS("User with given email already exists"),
    INVALID_EMAIL_FORMAT("Invalid email format"),
    ALL_FIELDS_MUST_BE_FILLED("All fields must be filled"),
    PASSWORDS_DOES_NOT_MATCH("Passwords must be equal"),
    INCORRECT_CREDENTIALS("Incorrect email/password"),

    TASK_DESCRIPTION_CANNOT_BE_EMPTY("Task description cannot be empty");


    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}
