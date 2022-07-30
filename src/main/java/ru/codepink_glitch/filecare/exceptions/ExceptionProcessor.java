package ru.codepink_glitch.filecare.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class ExceptionProcessor {

    @ExceptionHandler({ServiceException.class})
    public ResponseEntity<ApiError> handleException(ServiceException exception, WebRequest request) {
        ApiError error =
                new ApiError(HttpStatus.BAD_REQUEST, exception.getMessage(), exception.getExceptionsEnum().getMessage());
        return new ResponseEntity<>(error, new HttpHeaders(), error.getHttpStatus());
    }
}
