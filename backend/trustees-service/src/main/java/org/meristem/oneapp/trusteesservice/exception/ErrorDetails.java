package org.meristem.oneapp.trusteesservice.exception;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorDetails (LocalDateTime date, String message, String description, List<String> errors) {
}
