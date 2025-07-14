package org.meristem.oneapp.usersservice.domains.responses;

import lombok.Builder;

@Builder
public record UpdateResponse(String message, Boolean success) {
}
