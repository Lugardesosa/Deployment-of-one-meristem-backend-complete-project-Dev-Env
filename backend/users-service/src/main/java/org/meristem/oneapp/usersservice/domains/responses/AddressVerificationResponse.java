package org.meristem.oneapp.usersservice.domains.responses;

import lombok.Builder;

@Builder
public record AddressVerificationResponse(Boolean status, String message) {
}
