package org.meristem.oneapp.usersservice.domains.responses;

import lombok.Builder;

@Builder
public record VerifyOtpResponse(Boolean status, String message) {
}
