package org.meristem.oneapp.usersservice.domains.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import org.meristem.oneapp.usersservice.validations.constraints.Password;

@Builder
public record UpdatePasswordRequest(@Schema(name = "oldPassword", minLength = 8, description = "Pass a valid old password, 8 or more characters with upper case and special characters '?=.*[@#$%^&+=]'", maxLength = 20) @Password @NotBlank String oldPassword,
                                    @Schema(name = "newPassword", minLength = 8, description = "Pass a valid new password, 8 or more characters with upper case and special characters '?=.*[@#$%^&+=]'", maxLength = 20) @Password @NotBlank String newPassword) {
}
