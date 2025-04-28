package org.meristem.oneapp.usersservice.domains.responses;

import lombok.Builder;

@Builder
public record UpdatePhoneNumberResponse(Boolean status, String message) {
}
