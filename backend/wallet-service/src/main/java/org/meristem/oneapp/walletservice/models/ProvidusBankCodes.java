package org.meristem.oneapp.walletservice.models;


import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.Objects;

@ToString
@Getter
@Setter
@NoArgsConstructor
@Table("providus_bank_codes")
public class ProvidusBankCodes extends BaseModel<String> {

    @NotNull(message = "Cannot be null")
    private String bankCode;

    @NotNull(message = "Cannot be null")
    private String bankName;

    public ProvidusBankCodes(Long id, LocalDateTime createdDate, String createdBy, LocalDateTime lastModifiedDate, String lastModifiedBy, Integer version,
                             String bankCode, String bankName) {
        super(id, createdDate, createdBy, lastModifiedDate, lastModifiedBy, version);
        this.bankCode = bankCode;
        this.bankName = bankName;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ProvidusBankCodes that = (ProvidusBankCodes) o;
        return Objects.equals(getBankCode(), that.getBankCode()) && Objects.equals(getBankName(), that.getBankName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBankCode(), getBankName());
    }
}
