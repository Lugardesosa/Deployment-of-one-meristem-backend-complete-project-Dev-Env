package org.meristem.oneapp.walletservice.models;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.Objects;

@Table("provider_banks")
@NoArgsConstructor
@Getter
@Setter
public class ProviderBanks extends BaseModel<String> {

    private String bankCode;
    private String providerId;
    private String bankId;


    @Builder
    public ProviderBanks(Long id, LocalDateTime createdDate, String createdBy, LocalDateTime lastModifiedDate, String lastModifiedBy, Integer version, String bankCode, String providerId, String bankId) {
        super(id, createdDate, createdBy, lastModifiedDate, lastModifiedBy, version);
        this.bankCode = bankCode;
        this.providerId = providerId;
        this.bankId = bankId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ProviderBanks that = (ProviderBanks) o;
        return Objects.equals(getProviderId(), that.getProviderId()) && Objects.equals(getBankId(), that.getBankId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getProviderId(), getBankId());
    }
}
