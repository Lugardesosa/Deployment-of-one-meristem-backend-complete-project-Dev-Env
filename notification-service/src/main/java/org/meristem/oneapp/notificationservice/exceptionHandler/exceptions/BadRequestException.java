package org.meristem.oneapp.notificationservice.exceptionHandler.exceptions;

public class BadRequestException extends RuntimeException{

    public BadRequestException(String message) {
        super(message);
    }
}
