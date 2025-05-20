package org.meristem.oneapp.usersservice.exceptionHandler.exceptions;

import java.io.Serial;

public class UpstreamServiceException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 18792783682918L;
    public UpstreamServiceException(String s) {
    }
}
