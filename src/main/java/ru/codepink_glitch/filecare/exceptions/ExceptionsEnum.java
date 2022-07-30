package ru.codepink_glitch.filecare.exceptions;

/**
 * Enumerating exceptions with message
 */

public enum ExceptionsEnum {

    BAD_AUTHENTICATION_DATA("Wrong authentication data provided"),
    TOKEN_EXPIRED("Authentication token expired"),
    USER_NOT_FOUND("User not found by username"),
    EXCEPTION_SAVING_USER("Something went wrong, user was not saved"),
    NOT_A_DIRECTORY("Provided path is not a directory"),
    NOT_A_FILE("Provided path is not a file"),
    FILE_NOT_FOUND("Provided path contains no file"),
    FILE_DOWNLOAD_EXCETPION("Exception during file download"),
    FILE_NOT_PRESENT("Request don't contain any files to upload"),
    FILE_UPLOAD_EXCEPTION("Exception during file upload"),
    DIRECTORY_CREATE_EXCEPTION("Exception creating directory");

    private final String message;

    ExceptionsEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
