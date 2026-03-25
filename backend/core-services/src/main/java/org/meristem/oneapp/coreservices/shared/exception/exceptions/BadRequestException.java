package org.meristem.oneapp.coreservices.shared.exception.exceptions;

public class BadRequestException extends RuntimeException{

    public BadRequestException(String message) {
        super(message);
    }
}
