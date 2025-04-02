package org.meristem.oneapp.usersservice.domains.requests;

import jakarta.validation.constraints.*;

public record VerifyOtpRequest(@Min(value = 1000000, message = "cannot be less than 1000000") @Max(value = 9999999, message = "cannot be more than 9999999") @NotNull(message = "otp cannot be null") Integer otp,
                               @NotNull(message = "not null") @Max(value = 1, message = "not more than 1") @Min(value = 1, message = "not less than 1") Integer otpType,
                               @NotNull(message = "cannot be null") @Size(max = 200, message = "cannot be more than 200 chars") String recipient) {
}
