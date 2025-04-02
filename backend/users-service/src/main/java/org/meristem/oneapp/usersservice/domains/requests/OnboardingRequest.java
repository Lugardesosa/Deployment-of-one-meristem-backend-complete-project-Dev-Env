package org.meristem.oneapp.usersservice.domains.requests;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.lang.NonNull;

// TODO: remove userId from the request and get it from the logged in user data
public record OnboardingRequest(@NotNull(message = "Cannot be null") Long featureId,
                                @NotNull(message = "cannot be null") Long userId) {
}
