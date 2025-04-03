package org.meristem.oneapp.usersservice.domains.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import org.meristem.oneapp.usersservice.domains.enums.OtpType;

/**
 * OtpType should be increased to reflect the changes made to it
 * @param otp
 * @param otpType
 * @param recipient
 */
public record VerifyOtpRequest(@Min(value = 10000, message = "cannot be less than 10000") @Max(value = 99999, message = "cannot be more than 99999") @NotNull(message = "otp cannot be null") Integer otp,
                               @Schema(name = "Otp type", exampleClasses = {OtpType.class}) @NotNull(message = "not null") @Max(value = 2, message = "not more than 2") @Min(value = 1, message = "not less than 1") Integer otpType,
                               @NotNull(message = "cannot be null") @Size(max = 200, message = "cannot be more than 200 chars") String recipient) {
}
