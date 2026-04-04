package org.meristem.oneapp.wealthservice.models;

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
@Table("investment_plans")
public class InvestmentPlans extends BaseModel<String> {

    private String name;
    private String uuid;
    private String slug;
    private String shortName;
    private String videoUrl;
    private String coreProductId;
    private String coreFundId;
    private String description;
    private Integer position;
    private Boolean isActive;

    @Builder
    public InvestmentPlans(Long id, LocalDateTime createdDate, String createdBy, LocalDateTime lastModifiedDate, String lastModifiedBy, Integer version, Integer status, String name, String uuid, String slug, String shortName, String videoUrl, String description, Integer position, Boolean isActive, String coreProductId, String coreFundId) {
        super(id, createdDate, createdBy, lastModifiedDate, lastModifiedBy, version, status);
        this.name = name;
        this.uuid = uuid;
        this.slug = slug;
        this.shortName = shortName;
        this.videoUrl = videoUrl;
        this.description = description;
        this.position = position;
        this.isActive = isActive;
        this.coreProductId = coreProductId;
        this.coreFundId = coreFundId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        InvestmentPlans that = (InvestmentPlans) o;
        return Objects.equals(getUuid(), that.getUuid()) && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUuid(), getId());
    }
}