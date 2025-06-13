package org.meristem.oneapp.usersservice.domains.responses;

import lombok.Builder;

@Builder
public record DobResponse(Boolean status, String message) {
}
