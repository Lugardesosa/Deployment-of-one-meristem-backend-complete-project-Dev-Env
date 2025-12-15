package org.meristem.oneapp.usersservice.domains.requests;

import jakarta.validation.constraints.NotBlank;

public record VerifyPasswordRequest(@NotBlank(message = "Kindly enter the user's pin") String password) {
}
