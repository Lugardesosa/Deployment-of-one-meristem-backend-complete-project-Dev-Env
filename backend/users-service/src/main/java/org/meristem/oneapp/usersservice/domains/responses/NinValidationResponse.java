package org.meristem.oneapp.usersservice.domains.responses;

import lombok.Builder;

@Builder
public record NinValidationResponse(String message, Boolean success) {
}
