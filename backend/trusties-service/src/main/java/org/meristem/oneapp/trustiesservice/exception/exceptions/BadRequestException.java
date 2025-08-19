package org.meristem.oneapp.trustiesservice.exception.exceptions;

public class BadRequestException extends RuntimeException{

    public BadRequestException(String message) {
        super(message);
    }
}
