package org.meristem.oneapp.walletservice.models;

import lombok.*;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@ToString
@NoArgsConstructor
@Setter
@Getter
@Table("wallets")
public class Wallets extends BaseModel<String> {

    private BigDecimal balance;

    private String customerId;
    private String walletId;
    private String symplusAccountNo;

    @Builder
    public Wallets(Long id, LocalDateTime createdDate, String createdBy, LocalDateTime lastModifiedDate, String lastModifiedBy, Integer version,
                   String customerId, String walletId, String symplusAccountNo) {
        super(id, createdDate, createdBy, lastModifiedDate, lastModifiedBy, version);
        this.balance = BigDecimal.ZERO;
        this.customerId = customerId;
        this.walletId = walletId;
        this.symplusAccountNo = symplusAccountNo;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Wallets wallets = (Wallets) o;
        return Objects.equals(getCustomerId(), wallets.getCustomerId()) && Objects.equals(getWalletId(), wallets.getWalletId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCustomerId(), getWalletId());
    }
}
