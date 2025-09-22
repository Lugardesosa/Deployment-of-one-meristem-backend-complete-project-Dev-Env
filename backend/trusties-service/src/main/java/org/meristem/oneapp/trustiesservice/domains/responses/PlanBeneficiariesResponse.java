package org.meristem.oneapp.trustiesservice.domains.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.Objects;

@Builder
@Schema(
        name = "PlanBeneficiariesResponse",
        description = "Represents a beneficiary assigned to a plan and the percentage allocated to them.",
        example = "{\"beneficiaryId\": 123456789, \"beneficiaryPercent\": 50.0}"
)
public record PlanBeneficiariesResponse(
        @Schema(description = "Unique identifier of the beneficiary.", example = "123456789")
        Long beneficiaryId,
        @Schema(description = "Allocation percentage assigned to this beneficiary.", example = "50.0", minimum = "0", maximum = "100")
        Double beneficiaryPercent
) {
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PlanBeneficiariesResponse that = (PlanBeneficiariesResponse) o;
        return Objects.equals(beneficiaryId(), that.beneficiaryId()) && Objects.equals(beneficiaryPercent(), that.beneficiaryPercent());
    }

    @Override
    public int hashCode() {
        return Objects.hash(beneficiaryId(), beneficiaryPercent());
    }
}
