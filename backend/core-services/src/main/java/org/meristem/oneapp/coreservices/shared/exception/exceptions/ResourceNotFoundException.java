package org.meristem.oneapp.coreservices.shared.exception.exceptions;

import lombok.Getter;

import java.io.Serial;

@Getter
public class ResourceNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 191892892828L;

    private final String resourceName;
    private final String resourcePassed;
    public ResourceNotFoundException(String message, String resourceName, String resourcePassed) {
        super(message);
        this.resourceName = resourceName;
        this.resourcePassed = resourcePassed;
    }
}
