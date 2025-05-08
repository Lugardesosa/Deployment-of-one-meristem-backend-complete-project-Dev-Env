package org.meristem.oneapp.usersservice.domains.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import org.meristem.oneapp.usersservice.constants.AppConstants;
import org.meristem.oneapp.usersservice.validations.constraints.PinMatch;

@PinMatch
@Builder
public record PinRequest(@Schema(example = "0923", description = "enter the user's pin. Must not use repeating or consecutive numbers")
                               @NotBlank(message = "cannot be blank") @Pattern(regexp = AppConstants.PIN_REGEX_PATTERN, message = "Must not use repeating or consecutive numbers") String pin,
                         @NotBlank(message = "Cannot be blank") String confirmPin) {
}
