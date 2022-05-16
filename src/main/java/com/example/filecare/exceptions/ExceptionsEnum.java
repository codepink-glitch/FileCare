package com.example.filecare.exceptions;

/**
 * Enumerating exceptions with message
 */

public enum ExceptionsEnum {

    BAD_AUTHENTICATION_DATA("Wrong authentication data provided"),
    TOKEN_EXPIRED("Token expired"),
    USER_NOT_FOUND("User not found by username"),
    EXCEPTION_SAVING_USER("Something went wrong, user not saved");

    private final String message;

    ExceptionsEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
