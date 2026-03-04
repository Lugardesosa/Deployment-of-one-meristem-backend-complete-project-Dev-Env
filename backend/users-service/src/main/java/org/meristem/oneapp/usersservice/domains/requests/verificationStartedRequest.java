package org.meristem.oneapp.usersservice.domains.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Schema(name = "SmileIdIdRequest", description = "Request payload containing identifiers required to start or fetch a Smile Identity verification job.")
@Builder
public record verificationStartedRequest(
        @Schema(description = "Internal requirement identifier associated with the verification flow.", example = "12345")
        @NotNull(message = "requirementId cannot be null") Long requirementId
) {
}
