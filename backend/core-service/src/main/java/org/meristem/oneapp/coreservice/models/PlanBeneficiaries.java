package org.meristem.oneapp.coreservice.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
@Table("plan_beneficiaries")
public class PlanBeneficiaries extends BaseModel<String> {

    @NotNull(message = "Not blank")
    private Long planId;

    @NotNull(message = "Not blank")
    private Long beneficiaryId;

    @NotBlank(message = "Not blank")
    private String planType;

    private Double percentage;

    @Builder
    public PlanBeneficiaries(Long id, LocalDateTime createdDate, String createdBy, LocalDateTime lastModifiedDate, String lastModifiedBy, Integer version, Long planId, Long beneficiaryId, String planType, Double percentage) {
        super(id, createdDate, createdBy, lastModifiedDate, lastModifiedBy, version);
        this.planId = planId;
        this.beneficiaryId = beneficiaryId;
        this.planType = planType;
        this.percentage = percentage;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PlanBeneficiaries that = (PlanBeneficiaries) o;
        return Objects.equals(getPlanId(), that.getPlanId()) && Objects.equals(getBeneficiaryId(), that.getBeneficiaryId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPlanId(), getBeneficiaryId());
    }
}
