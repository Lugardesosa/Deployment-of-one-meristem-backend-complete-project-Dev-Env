package org.meristem.oneapp.usersservice.domains.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.meristem.oneapp.usersservice.domains.enums.MessageMedium;
import org.meristem.oneapp.usersservice.domains.enums.MessageSubject;

/**
 * OtpType should be increased to reflect the changes made to it
 * @param otp
 * @param otpType
 * @param recipient
 */
public record VerifyOtpRequest(@Schema(example = "3493", description = "The otp the user received") @Min(value = 1000, message = "cannot be less than 1000") @Max(value = 9999, message = "cannot be more than 9999") @NotNull(message = "otp cannot be null") Integer otp,
                               @Schema(implementation = MessageSubject.class, allowableValues = {"1", "2", "3", "4", "5", "6", "7"}, name = "otpType", anyOf = {MessageSubject.class}, example = "1", description = "Pass the same otpType that was passed when the otp was sent") @NotNull(message = "not null") @Max(value = 7, message = "not more than 7") @Min(value = 1, message = "not less than 1") Integer otpType,
                               @Schema(name = "recipient", example = "james@gmail.com", description = "Pass the same recipient that the otp was sent to") @NotNull(message = "cannot be null") @Size(max = 200, message = "cannot be more than 200 chars") String recipient,
                               String key,
                               @Schema(anyOf = {MessageMedium.class}, example = "1", allowableValues = {"1", "2", "3"}, description = "use 1 for email, 2 for sms, and 3 for whatsapp") @NotNull(message = "message medium must be passed") @Max(value = 3, message = "Cannot be higher than 3") @Min(value = 1, message = "Cannot be less than 1") Integer messageMedium) {
}
