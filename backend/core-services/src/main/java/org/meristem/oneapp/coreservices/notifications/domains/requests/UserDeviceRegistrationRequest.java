package org.meristem.oneapp.coreservices.notifications.domains.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record UserDeviceRegistrationRequest(@NotBlank(message = "Cannot be blank") String expoToken, @NotBlank(message = "Cannot be blank") String deviceId) {
}
