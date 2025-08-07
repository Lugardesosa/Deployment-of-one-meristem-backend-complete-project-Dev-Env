package org.meristem.oneapp.coreservice.models;


import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
@Table("money_market")
public class MoneyMarket extends Assets {

    @NotBlank(message = "Cannot be null")
    @Column("asset_type")
    private String assetType;

    @NotBlank(message = "Cannot be null")
    @Column("investment_house")
    private String investmentHouse;

    @Builder
    public MoneyMarket(Long id, LocalDateTime createdDate, String createdBy, LocalDateTime lastModifiedDate, String lastModifiedBy, Integer version, Long ownerId, BigDecimal estimatedAmount, Long currencyId, String otherDetails, String assetType, String investmentHouse) {
        super(id, createdDate, createdBy, lastModifiedDate, lastModifiedBy, version, ownerId, estimatedAmount, currencyId, otherDetails);
        this.assetType = assetType;
        this.investmentHouse = investmentHouse;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        MoneyMarket that = (MoneyMarket) o;
        return Objects.equals(getAssetType(), that.getAssetType()) && Objects.equals(getInvestmentHouse(), that.getInvestmentHouse());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAssetType(), getInvestmentHouse());
    }
}
