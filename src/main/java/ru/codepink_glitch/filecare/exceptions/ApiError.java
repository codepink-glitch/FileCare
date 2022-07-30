package ru.codepink_glitch.filecare.exceptions;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ApiError {

    HttpStatus httpStatus;
    String message;
    List<String> errors;

    public ApiError(HttpStatus httpStatus, String message, String error) {
        this.httpStatus = httpStatus;
        this.message = message;
        this.errors = Collections.singletonList(error);
    }
}
