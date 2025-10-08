package org.meristem.oneapp.usersservice.domains.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record StateUpdateRequest(@NotNull(message = "Pass the id of the country")
                                 @Schema(description = "The unique identifier of the country", example = "123")
                                 Long countryId,
                                 @NotNull(message = "Pass the id of the state")
                                 @Schema(description = "The unique identifier of the state", example = "123")
                                 Long stateId) {
}
