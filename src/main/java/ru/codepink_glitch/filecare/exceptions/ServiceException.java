package ru.codepink_glitch.filecare.exceptions;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ServiceException extends RuntimeException {

    ExceptionsEnum exceptionsEnum;

    public ServiceException(ExceptionsEnum exceptionsEnum) {
        super(exceptionsEnum.getMessage());
        this.exceptionsEnum = exceptionsEnum;
    }
}
