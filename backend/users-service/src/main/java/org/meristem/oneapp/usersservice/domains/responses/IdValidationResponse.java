package org.meristem.oneapp.usersservice.domains.responses;

import lombok.Builder;

@Builder
public record IdValidationResponse(String message, Boolean success) {
}
