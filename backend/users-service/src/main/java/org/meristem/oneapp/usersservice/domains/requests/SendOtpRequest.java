package org.meristem.oneapp.usersservice.domains.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * MessageMedium and OtpType should be updated to match its current state
 * @param otpType
 * @param recipient
 * @param messageMedium
 */
// TODO: change otp type as it changes in the enum
@JsonIgnoreProperties(ignoreUnknown = true)
public record SendOtpRequest(@NotNull(message = "otp type must be passed") @Max(value = 2, message = "Cannot be higher than 2") @Min(value = 1, message = "Cannot be less than 1") Integer otpType,
                             @NotNull(message = "cannot be null") @Size(max = 200, message = "cannot be more than 200 chars") String recipient,
                             @NotNull(message = "otp type must be passed") @Max(value = 3, message = "Cannot be higher than 3") @Min(value = 1, message = "Cannot be less than 1") Integer messageMedium) {

    @Override
    public String recipient() {
        return recipient.contains("@") ? recipient : recipient.replace("+", "").replaceAll("^234", "0");
    }
}
