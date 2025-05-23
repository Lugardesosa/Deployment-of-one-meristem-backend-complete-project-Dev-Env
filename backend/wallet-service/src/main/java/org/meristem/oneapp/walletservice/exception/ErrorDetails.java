package org.meristem.oneapp.walletservice.exception;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorDetails (LocalDateTime date, String message, String description, List<String> errors) {
}
