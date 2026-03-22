package org.meristem.oneapp.coreservices.notifications.domains.requests;

import jakarta.validation.constraints.NotBlank;

public record UserDeviceUpdateRequest(@NotBlank(message = "Cannot be blank") String deviceId) {
}
