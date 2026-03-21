package org.meristem.oneapp.usersservice.domains.requests;


import jakarta.validation.constraints.NotNull;

public record BiometricLoginUpdateRequest(@NotNull(message = "Cannot be null") boolean biometricLogin) {
}
