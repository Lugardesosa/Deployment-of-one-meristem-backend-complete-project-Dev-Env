package org.meristem.oneapp.kafka.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

// TODO: change otp type as it changes in the enum
@JsonIgnoreProperties(ignoreUnknown = true)
public record SendOtpRequest(@NotNull(message = "otp type must be passed") @Max(value = 1, message = "Cannot be higher than 1") @Min(value = 1, message = "Cannot be less than 1") Integer otpType,
                             @NotNull(message = "cannot be null") @Size(max = 200, message = "cannot be more than 200 chars") String recipient) {
}
