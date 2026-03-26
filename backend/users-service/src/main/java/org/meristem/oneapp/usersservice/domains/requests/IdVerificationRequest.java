package org.meristem.oneapp.usersservice.domains.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import org.meristem.oneapp.usersservice.domains.enums.Vendor;

@Schema(name = "SmileIdIdRequest", description = "Request payload containing identifiers required to start or fetch a Smile Identity verification job.")
@Builder
public record IdVerificationRequest(
        @Schema(description = "Internal requirement identifier associated with the verification flow.", example = "12345")
        @NotNull(message = "investmentRequirementId cannot be null") Long investmentRequirementId,
        @Schema(description = "Smile Identity job identifier returned by the provider.", example = "job_01HZY6E0KQ3T7N9J2C4A")
        @NotBlank(message = "jobId cannot be blank") String jobId,
        @Schema(pattern = "^[0-9]{11}$", example = "12345678901") @Pattern(regexp = "^[0-9]{11}$", message = "Pass a valid bvn") @NotBlank(message = "Pass a valid bvn") String idNumber
) {
}
