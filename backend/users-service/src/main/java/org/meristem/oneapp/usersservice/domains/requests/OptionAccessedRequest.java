package org.meristem.oneapp.usersservice.domains.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record OptionAccessedRequest(@Schema(example = "1", description = "The option id") @NotNull(message = "Cannot be null") Long optionId) {
}
