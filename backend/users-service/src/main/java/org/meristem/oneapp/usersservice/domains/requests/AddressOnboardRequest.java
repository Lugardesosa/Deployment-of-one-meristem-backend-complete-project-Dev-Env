package org.meristem.oneapp.usersservice.domains.requests;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record AddressOnboardRequest(@NotNull(message = "cannot be null") Long requirementId) {
}
