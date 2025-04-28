package org.meristem.oneapp.usersservice.domains.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import org.meristem.oneapp.usersservice.constants.AppConstants;

@Builder
public record CreatePinRequest(@Schema(example = "0923", description = "enter the user's pin. Must not use repeating characters or 0123 and 1234")
                               @NotBlank(message = "cannot be blank") @Pattern(regexp = AppConstants.PIN_REGEX_PATTERN, message = "Must not use repeating characters or 0123 and 1234") String pin) {
}
