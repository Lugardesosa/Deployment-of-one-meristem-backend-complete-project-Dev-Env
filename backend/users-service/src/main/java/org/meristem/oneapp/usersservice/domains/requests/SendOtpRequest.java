package org.meristem.oneapp.usersservice.domains.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.meristem.oneapp.usersservice.domains.enums.MessageMedium;
import org.meristem.oneapp.usersservice.domains.enums.MessageSubject;

/**
 * MessageMedium and OtpType should be updated to match its current state
 * @param otpType
 * @param recipient
 * @param messageMedium
 */
// TODO: change otp type as it changes in the enum
@JsonIgnoreProperties(ignoreUnknown = true)
public record SendOtpRequest(@Schema(anyOf = {MessageSubject.class}, example = "1", allowableValues = {"1", "2", "3", "4", "5", "6"}, description = "use 1 for otp send before registration and 2 for otp sent for password reset, 3 for registration flow") @NotNull(message = "otp type must be passed") @Max(value = 6, message = "Cannot be higher than 3") @Min(value = 1, message = "Cannot be less than 1") Integer otpType,
                             @Schema(example = "james@gmail.com", description = "If medium is 1, pass email, if 2 or 3, pass phone number") @NotNull(message = "cannot be null") @Size(max = 200, message = "cannot be more than 200 chars") String recipient,
                             @Schema(anyOf = {MessageMedium.class}, example = "1", allowableValues = {"1", "2", "3"}, description = "use 1 for email, 2 for sms, and 3 for whatsapp") @NotNull(message = "message medium must be passed") @Max(value = 3, message = "Cannot be higher than 3") @Min(value = 1, message = "Cannot be less than 1") Integer messageMedium) {

    @Override
    public String recipient() {
        return recipient.contains("@") ? recipient : recipient.replace("+", "");
    }
}
