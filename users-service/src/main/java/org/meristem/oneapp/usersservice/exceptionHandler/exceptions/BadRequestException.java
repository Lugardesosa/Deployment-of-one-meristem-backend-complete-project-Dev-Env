package org.meristem.oneapp.usersservice.exceptionHandler.exceptions;

public class BadRequestException extends RuntimeException{

    public BadRequestException(String message) {
        super(message);
    }
}
