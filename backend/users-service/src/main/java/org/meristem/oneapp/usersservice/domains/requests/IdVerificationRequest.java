package org.meristem.oneapp.usersservice.domains.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.meristem.oneapp.usersservice.domains.enums.Vendor;

@Schema(name = "SmileIdIdRequest", description = "Request payload containing identifiers required to start or fetch a Smile Identity verification job.")
@Builder
public record IdVerificationRequest(
        @Schema(description = "The vendor name.", example = "SMILE_ID", anyOf = Vendor.class)
        @NotNull(message = "requirementId cannot be null") Vendor vendor,
        @Schema(description = "Internal requirement identifier associated with the verification flow.", example = "12345")
        @NotNull(message = "requirementId cannot be null") Long requirementId,
        @Schema(description = "Smile Identity job identifier returned by the provider.", example = "job_01HZY6E0KQ3T7N9J2C4A")
        @NotBlank(message = "jobId cannot be blank") String jobId
) {
}
