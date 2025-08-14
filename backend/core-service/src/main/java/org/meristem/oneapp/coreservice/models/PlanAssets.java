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
@Table("plan_assets")
public class PlanAssets extends BaseModel<String>{

    @NotNull(message = "Not blank")
    private Long planId;

    @NotNull(message = "Not blank")
    private Long assetId;

    @NotBlank(message = "Not blank")
    private String planType;

    @NotBlank(message = "Not blank")
    private String assetType;

    @Builder
    public PlanAssets(Long id, LocalDateTime createdDate, String createdBy, LocalDateTime lastModifiedDate, String lastModifiedBy, Integer version, Long planId, Long assetId, String planType, String assetType) {
        super(id, createdDate, createdBy, lastModifiedDate, lastModifiedBy, version);
        this.planId = planId;
        this.assetId = assetId;
        this.planType = planType;
        this.assetType = assetType;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PlanAssets that = (PlanAssets) o;
        return Objects.equals(getPlanId(), that.getPlanId()) && Objects.equals(getAssetId(), that.getAssetId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPlanId(), getAssetId());
    }
}
