package org.meristem.oneapp.usersservice.domains.responses;

import lombok.Builder;

@Builder
public record GenderResponse(Boolean status, String message) {
}
