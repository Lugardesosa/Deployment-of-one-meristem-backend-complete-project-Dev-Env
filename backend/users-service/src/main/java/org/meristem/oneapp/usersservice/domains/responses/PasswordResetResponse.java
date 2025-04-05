package org.meristem.oneapp.usersservice.domains.responses;

import lombok.Builder;

@Builder
public record PasswordResetResponse(String message, Boolean success) {
}
