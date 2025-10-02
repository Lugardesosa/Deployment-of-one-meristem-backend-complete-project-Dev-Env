package org.meristem.oneapp.trusteesservice.exception.exceptions;

public class BadRequestException extends RuntimeException{

    public BadRequestException(String message) {
        super(message);
    }
}
