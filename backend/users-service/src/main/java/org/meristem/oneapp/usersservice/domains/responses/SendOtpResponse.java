package org.meristem.oneapp.usersservice.domains.responses;

import lombok.Builder;

@Builder
public record SendOtpResponse(String message, String recipient, Integer timeToExpireInSeconds) {
}
