package org.meristem.oneapp.usersservice.domains.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import org.meristem.oneapp.usersservice.validations.constraints.Password;
import org.meristem.oneapp.usersservice.validations.constraints.PasswordMatch;

// TODO: DELETE recipient AND GET IT FROM THE SECURITY CONTEXT
@PasswordMatch
public record PasswordResetRequest(@Schema(name = "password", minLength = 8) @Password @NotBlank String password, @Password @NotBlank String confirmPassword, String recipient) {
    @Override
    public String recipient() {
        return recipient.contains("@") ? recipient : recipient.replace("+", "").replaceAll("^234", "0");
    }
}
