package org.meristem.oneapp.wealthservice.exception;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorDetails (LocalDateTime date, String message, String description, List<String> errors) {
}
