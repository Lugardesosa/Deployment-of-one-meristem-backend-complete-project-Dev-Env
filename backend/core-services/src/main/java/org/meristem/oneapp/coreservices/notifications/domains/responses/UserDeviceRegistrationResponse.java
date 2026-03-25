package org.meristem.oneapp.coreservices.notifications.domains.responses;

import lombok.Builder;

@Builder
public record UserDeviceRegistrationResponse(String message, Boolean status) {
}
