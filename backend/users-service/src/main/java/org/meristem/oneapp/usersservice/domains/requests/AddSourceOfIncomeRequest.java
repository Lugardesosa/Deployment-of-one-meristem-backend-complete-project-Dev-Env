package org.meristem.oneapp.usersservice.domains.requests;

import jakarta.validation.constraints.NotBlank;

public record AddSourceOfIncomeRequest(@NotBlank(message = "Cannot be blank") String name) {
}
