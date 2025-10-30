package org.meristem.oneapp.usersservice.domains.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import org.meristem.oneapp.usersservice.validations.constraints.Password;
import org.meristem.oneapp.usersservice.validations.constraints.PasswordMatch;

@PasswordMatch
public record PasswordResetRequest(@Schema(name = "password", minLength = 8, description = "Pass a valid password, 8 or more characters with upper case and special characters '?=.*[@#$%^&+=]'", maxLength = 20) @Password @NotBlank String password,
                                   @Schema(name = "confirmPassword", minLength = 8, description = "Pass a valid password, 8 or more characters with upper case and special characters '?=.*[@#$%^&+=]', must match the first password", maxLength = 20) @Password @NotBlank String confirmPassword,
                                   @Schema(example = "johndoe@gmail.com", description = "Pass the user's email or phone number") @NotBlank(message = "must not be null") String recipient) {
    @Override
    public String recipient() {
        return recipient.contains("@") ? recipient : recipient.replace("+", "").replaceAll("^234", "0");
    }
}
