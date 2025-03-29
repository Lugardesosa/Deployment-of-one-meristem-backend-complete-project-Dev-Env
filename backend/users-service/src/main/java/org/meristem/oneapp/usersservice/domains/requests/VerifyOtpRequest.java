package org.meristem.oneapp.usersservice.domains.requests;

import jakarta.validation.constraints.*;

public record VerifyOtpRequest(@NotNull(message = "otp cannot be null") Integer otp,
                               @NotNull(message = "not null") @Max(value = 1, message = "not more than 1") @Min(value = 1, message = "not less than 1") Integer otpType,
                               @NotNull(message = "cannot be null") @Size(max = 200, message = "cannot be more than 200 chars") String recipient) {
}
