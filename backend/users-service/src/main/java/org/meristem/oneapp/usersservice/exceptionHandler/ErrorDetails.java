package org.meristem.oneapp.usersservice.exceptionHandler;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public record ErrorDetails (LocalDateTime date, String message, String description, List<String> errors) {
}
