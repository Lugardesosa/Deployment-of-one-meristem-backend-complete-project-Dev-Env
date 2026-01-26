package org.meristem.oneapp.usersservice.domains.requests;

import jakarta.validation.constraints.NotNull;

public record EnableAdminRequest(@NotNull(message = "Cannot be null") Long userId, @NotNull(message = "Cannot be null") Boolean enableAdmin) {
}
