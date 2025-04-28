package org.meristem.oneapp.usersservice.domains.requests;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record AddressOnboardRequest(@NotNull(message = "cannot be null") Boolean approve, @NotNull(message = "cannot be blank") Long userId, @NotNull(message = "cannot be null") Long requirementId) {
}
