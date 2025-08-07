package org.meristem.oneapp.coreservice.models;


import jakarta.validation.constraints.NotNull;
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
@Table("fintech_wallets")
public class FintechWallets extends Assets {

    @NotNull(message = "Cannot be null")
    @Column("fintech_app")
    private String fintechApp;

    @NotNull(message = "Cannot be null")
    @Column("uniqueId")
    private String uniqueId;

    @Builder
    public FintechWallets(Long id, LocalDateTime createdDate, String createdBy, LocalDateTime lastModifiedDate, String lastModifiedBy, Integer version, Long ownerId, BigDecimal estimatedAmount, Long currencyId, String otherDetails, String fintechApp, String uniqueId) {
        super(id, createdDate, createdBy, lastModifiedDate, lastModifiedBy, version, ownerId, estimatedAmount, currencyId, otherDetails);
        this.fintechApp = fintechApp;
        this.uniqueId = uniqueId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        FintechWallets that = (FintechWallets) o;
        return Objects.equals(getFintechApp(), that.getFintechApp()) && Objects.equals(getUniqueId(), that.getUniqueId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFintechApp(), getUniqueId());
    }
}
