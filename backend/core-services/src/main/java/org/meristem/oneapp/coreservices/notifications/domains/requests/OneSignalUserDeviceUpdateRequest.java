package org.meristem.oneapp.coreservices.notifications.domains.requests;

import jakarta.validation.constraints.NotBlank;

public record OneSignalUserDeviceUpdateRequest(@NotBlank(message = "Cannot be blank") String deviceId, @NotBlank(message = "Cannot be blank") String subscriptionId) {
}
