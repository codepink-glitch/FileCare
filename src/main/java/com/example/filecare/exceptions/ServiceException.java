package com.example.filecare.exceptions;

import lombok.Data;

@Data
public class ServiceException extends RuntimeException {

    private final ExceptionsEnum exceptionsEnum;

    public ServiceException(ExceptionsEnum exceptionsEnum) {
        super(exceptionsEnum.getMessage());
        this.exceptionsEnum = exceptionsEnum;
    }
}
