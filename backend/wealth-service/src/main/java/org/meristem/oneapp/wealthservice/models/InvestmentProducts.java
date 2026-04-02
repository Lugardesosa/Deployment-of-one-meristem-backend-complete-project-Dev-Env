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
@Table("investment_products")
public class InvestmentProducts extends BaseModel<String> {

    private String name;
    private String uuid;
    private String slug;
    private String supportedCurrency;
    private String description;
    private Integer position;
    private Boolean isActive;

    @Builder
    public InvestmentProducts(Long id, LocalDateTime createdDate, String createdBy, LocalDateTime lastModifiedDate, String lastModifiedBy, Integer version, Integer status, String name, String uuid, String slug, String supportedCurrency, String description, Integer position, Boolean isActive) {
        super(id, createdDate, createdBy, lastModifiedDate, lastModifiedBy, version, status);
        this.name = name;
        this.uuid = uuid;
        this.slug = slug;
        this.supportedCurrency = supportedCurrency;
        this.description = description;
        this.position = position;
        this.isActive = isActive;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        InvestmentProducts that = (InvestmentProducts) o;
        return Objects.equals(getUuid(), that.getUuid()) && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUuid(), getId());
    }
}