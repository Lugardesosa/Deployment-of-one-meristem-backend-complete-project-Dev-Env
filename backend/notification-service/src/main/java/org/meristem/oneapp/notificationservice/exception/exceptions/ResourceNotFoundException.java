package org.meristem.oneapp.notificationservice.exception.exceptions;

import lombok.Getter;

import java.io.Serial;

@Getter
public class ResourceNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 191892892828L;

    private final String name;
    private final String resource;
    public ResourceNotFoundException(String message, String name, String resource) {
        super(message);
        this.name = name;
        this.resource = resource;
    }
}
