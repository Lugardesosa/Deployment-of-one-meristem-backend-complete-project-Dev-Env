package org.meristem.oneapp.usersservice.domains.requests;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record ProcessAddressRequest(@NotNull(message = "cannot be blank") Long userId, @NotNull(message = "cannot be null") Long requirementId) {
}
