package org.meristem.oneapp.usersservice.domains.responses;

import lombok.Builder;

@Builder
public record AccountDeactivationResponse(String message, boolean status) {
}
