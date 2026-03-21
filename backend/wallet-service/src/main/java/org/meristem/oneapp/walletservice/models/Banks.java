package org.meristem.oneapp.walletservice.models;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.Objects;

@Table("banks")
@NoArgsConstructor
@Getter
@Setter
public class Banks extends BaseModel<String> {

    private String bankName;
    private String bankCode;
    private String providerCode;
    private String providerName;

    @Builder
    public Banks(Long id, LocalDateTime createdDate, String createdBy, LocalDateTime lastModifiedDate, String lastModifiedBy, Integer version, String bankName, String bankCode, String providerCode, String providerName) {
        super(id, createdDate, createdBy, lastModifiedDate, lastModifiedBy, version);
        this.bankName = bankName;
        this.bankCode = bankCode;
        this.providerCode = providerCode;
        this.providerName = providerName;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Banks banks = (Banks) o;
        return Objects.equals(getBankCode(), banks.getBankCode()) && Objects.equals(getProviderCode(), banks.getProviderCode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBankCode(), getProviderCode());
    }
}
