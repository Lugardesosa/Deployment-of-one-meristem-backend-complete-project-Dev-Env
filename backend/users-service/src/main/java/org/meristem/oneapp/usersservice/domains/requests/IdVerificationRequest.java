package org.meristem.oneapp.usersservice.domains.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class IdVerificationRequest {


        @Schema(description = "Internal requirement identifier associated with the verification flow.", example = "12345")
        @NotNull(message = "investmentRequirementId cannot be null") private Long investmentRequirementId;
        @Schema(description = "Smile Identity job identifier returned by the provider.", example = "job_01HZY6E0KQ3T7N9J2C4A")
        @NotBlank(message = "jobId cannot be blank") private String jobId;
        @Schema(pattern = "^[0-9]{11}$", example = "12345678901") @Pattern(regexp = "^[0-9]{11}$", message = "Pass a valid bvn") @NotBlank(message = "Pass a valid bvn") private String idNumber;
        @Schema(description = "pass true if it is a secondary user") private boolean secondary;
        @Schema(description = "pass true if it is an existing meristem user") private boolean existingUser;
}
