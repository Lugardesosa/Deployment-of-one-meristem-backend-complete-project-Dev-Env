package org.meristem.oneapp.usersservice.domains.responses;

import lombok.Builder;

@Builder
public record PinResponse(Boolean status, String message) {
}
