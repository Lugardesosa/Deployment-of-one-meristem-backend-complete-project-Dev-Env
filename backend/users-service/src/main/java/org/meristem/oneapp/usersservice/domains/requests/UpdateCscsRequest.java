package org.meristem.oneapp.usersservice.domains.requests;

import jakarta.validation.constraints.NotBlank;

public record UpdateCscsRequest(@NotBlank(message = "Cannot be blank") String chnNumber) {
}
