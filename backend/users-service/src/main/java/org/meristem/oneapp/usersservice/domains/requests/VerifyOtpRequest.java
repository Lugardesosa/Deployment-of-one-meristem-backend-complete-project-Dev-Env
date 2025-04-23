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
public record VerifyOtpRequest(@Schema(example = "34934", description = "The otp the user received") @Min(value = 10000, message = "cannot be less than 10000") @Max(value = 99999, message = "cannot be more than 99999") @NotNull(message = "otp cannot be null") Integer otp,
                               @Schema(allowableValues = {"1", "2"}, name = "Otp type", anyOf = {OtpType.class}, example = "1", description = "Pass the same otpType that was passed when the otp was sent") @NotNull(message = "not null") @Max(value = 2, message = "not more than 2") @Min(value = 1, message = "not less than 1") Integer otpType,
                               @Schema(name = "recipient", example = "james@gmail.com", description = "Pass the same recipient that the otp was sent to") @NotNull(message = "cannot be null") @Size(max = 200, message = "cannot be more than 200 chars") String recipient) {
}
