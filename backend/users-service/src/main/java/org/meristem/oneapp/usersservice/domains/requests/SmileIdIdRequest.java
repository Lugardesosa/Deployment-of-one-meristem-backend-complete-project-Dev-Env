package org.meristem.oneapp.usersservice.domains.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "SmileIdIdRequest", description = "Request payload containing identifiers required to start or fetch a Smile Identity verification job.")
@Builder
public record SmileIdIdRequest(
        @Schema(description = "Internal requirement identifier associated with the verification flow.", example = "12345", required = true)
        @NotNull(message = "requirementId cannot be null") Long requirementId,
        @Schema(description = "Smile Identity job identifier returned by the provider.", example = "job_01HZY6E0KQ3T7N9J2C4A", required = true)
        @NotBlank(message = "jobId cannot be blank") String jobId
) {
}
