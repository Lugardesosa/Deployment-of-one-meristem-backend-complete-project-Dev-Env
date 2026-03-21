package org.meristem.oneapp.usersservice.domains.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.meristem.oneapp.usersservice.validations.constraints.Email;

public record ShareAllDataRequest(
        @Email(message = "enter a valid email address") String userEmail,
        @NotNull(message = "Cannot be null") Boolean dataSharing,
        @NotNull(message = "Cannot be null") Boolean marketingDataSharing,
        @NotNull(message = "Cannot be null") Boolean aiAndAnalyticsDataSharing
) {
}
