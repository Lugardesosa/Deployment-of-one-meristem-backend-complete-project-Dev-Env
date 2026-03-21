package org.meristem.oneapp.notificationservice.domains.responses;

import lombok.Builder;

@Builder
public record UserDeviceRegistrationResponse(String message, Boolean status) {
}
