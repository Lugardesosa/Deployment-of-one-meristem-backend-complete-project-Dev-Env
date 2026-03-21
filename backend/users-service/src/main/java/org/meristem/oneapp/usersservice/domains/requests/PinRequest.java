package org.meristem.oneapp.usersservice.domains.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import org.meristem.oneapp.usersservice.constants.AppConstants;

@Builder
public record PinRequest(@Schema(example = "0923", description = "enter the user's newPin. Must not use repeating or consecutive numbers")
                         @Pattern(regexp = AppConstants.PIN_REGEX_PATTERN, message = "Must not use repeating or consecutive numbers") String newPin,
                         @Schema(example = "8726", description = "enter the user's oldPin. Must not use repeating or consecutive numbers")
                         @Pattern(regexp = AppConstants.PIN_REGEX_PATTERN, message = "Must not use repeating or consecutive numbers") String oldPin,
                         @Schema(example = "1", description = "Enter 0 for creation and 1 for update")
                         @NotNull(message = "Enter 0 for creation and 1 for update") @Min(value = 0, message = "Cannot be less than 0") @Max(value = 1, message = "Cannot be more than 1") Integer isNew) {
}
