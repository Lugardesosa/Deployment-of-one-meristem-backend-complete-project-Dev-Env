package org.meristem.oneapp.coreservice.domains.responses;

import lombok.Builder;

import java.util.Objects;

@Builder
public record PlanBeneficiariesResponse(Long beneficiaryId, Double beneficiaryPercent) {
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
