package org.meristem.oneapp.usersservice.domains.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.meristem.oneapp.usersservice.validations.constraints.Email;

public record UpdateEmailRequest(
        @Schema(example = "johndoe@gmail.com", description = "Pass the users email") @Email @NotBlank(message = "cannot be null") @Size(min = 5, max = 200, message = "cannot be longer than 200 and less than 5") String newEmail
) {
}
