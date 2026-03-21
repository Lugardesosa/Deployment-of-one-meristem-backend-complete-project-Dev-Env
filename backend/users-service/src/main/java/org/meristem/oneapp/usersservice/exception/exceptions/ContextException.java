package org.meristem.oneapp.usersservice.exception.exceptions;

import lombok.Getter;

import java.io.Serial;
import java.util.List;

@Getter
public class ContextException extends RuntimeException {

    private final List<String> messages;

    @Serial
    private static final long serialVersionUID = 1918928928123L;

    public ContextException(String message, List<String> messages) {
        super(message);
        this.messages = messages;
    }
}
