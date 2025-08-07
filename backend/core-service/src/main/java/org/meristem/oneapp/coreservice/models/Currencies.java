package org.meristem.oneapp.coreservice.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
@Table("currencies")
public class Currencies extends BaseModel<String> {

    @Column("currency_name")
    private String currencyName;

    @Column("currency_logo")
    private String currencyLogo;

    public Currencies(Long id, LocalDateTime createdDate, String createdBy, LocalDateTime lastModifiedDate, String lastModifiedBy, Integer version, String currencyName, String currencyLogo) {
        super(id, createdDate, createdBy, lastModifiedDate, lastModifiedBy, version);
        this.currencyName = currencyName;
        this.currencyLogo = currencyLogo;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Currencies that = (Currencies) o;
        return Objects.equals(getCurrencyName(), that.getCurrencyName()) && Objects.equals(getCurrencyLogo(), that.getCurrencyLogo());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCurrencyName(), getCurrencyLogo());
    }
}
