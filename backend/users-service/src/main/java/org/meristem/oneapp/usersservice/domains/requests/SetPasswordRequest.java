package org.meristem.oneapp.usersservice.domains.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import org.meristem.oneapp.usersservice.domains.enums.PasswordSetType;
import org.meristem.oneapp.usersservice.validations.constraints.Email;
import org.meristem.oneapp.usersservice.validations.constraints.Password;
import org.meristem.oneapp.usersservice.validations.constraints.PasswordMatch;

@Builder
@PasswordMatch
public record SetPasswordRequest(
        @Schema(example = "johndoe@gmail.com", description = "Pass the users email") @Email @NotBlank(message = "cannot be null") @Size(min = 5, max = 200, message = "cannot be longer than 200 and less than 5") String email,
        @Schema(example = "Password@1", description = "Pass a valid password, 8 or more characters with upper case and any of the following special characters '@', '#', '$', '%', '^', '&', '+', '=', '(', ')', '\\''", minLength = 8, maxLength = 20) @Password @NotBlank(message = "cannot be null") @Size(min = 8, max = 20, message = "cannot be more than 20 and less than 8") String password,
        String confirmPassword,
        PasswordSetType passwordSetType
) {
}
