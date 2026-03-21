package org.meristem.oneapp.walletservice.models;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.Objects;

@Table("bank_details_provider")
@NoArgsConstructor
@Getter
@Setter
public class BankDetailsProvider extends BaseModel<String> {

    private String providerCode;
    private String providerName;

    @Builder
    public BankDetailsProvider(Long id, LocalDateTime createdDate, String createdBy, LocalDateTime lastModifiedDate, String lastModifiedBy, Integer version, String providerCode, String providerName) {
        super(id, createdDate, createdBy, lastModifiedDate, lastModifiedBy, version);
        this.providerCode = providerCode;
        this.providerName = providerName;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        BankDetailsProvider that = (BankDetailsProvider) o;
        return Objects.equals(providerCode, that.providerCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(providerCode);
    }
}
