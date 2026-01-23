package org.meristem.oneapp.usersservice.domains.requests;


import jakarta.validation.constraints.NotBlank;

public record VerifyPinRequest(@NotBlank(message = "Kindly enter the user's pin") String pin, Long userId) {
}
