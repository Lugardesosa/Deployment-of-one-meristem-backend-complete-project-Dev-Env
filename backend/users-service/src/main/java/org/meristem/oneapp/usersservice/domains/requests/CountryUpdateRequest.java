package org.meristem.oneapp.usersservice.domains.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Request object for updating country or state information")
public record CountryUpdateRequest(@NotNull(message = "Pass the id of the country")
                                        @Schema(description = "The unique identifier of the country", example = "123")
                                        Long id) {
}
