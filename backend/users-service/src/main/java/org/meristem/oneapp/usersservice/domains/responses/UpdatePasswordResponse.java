package org.meristem.oneapp.usersservice.domains.responses;

import lombok.Builder;

@Builder
public record UpdatePasswordResponse(String message, Boolean success) {
}
