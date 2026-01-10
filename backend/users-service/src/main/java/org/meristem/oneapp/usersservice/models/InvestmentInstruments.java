package org.meristem.oneapp.usersservice.models;

import lombok.*;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.Objects;



@ToString
@NoArgsConstructor
@Setter
@Getter
@Table("investment_instruments")
public class InvestmentInstruments extends BaseModel<String> {

    @Column("name")
    private String name;

    @Column("code")
    private String code;

    @Column("type")
    private Integer type;

    @Builder
    public InvestmentInstruments(Long id, LocalDateTime createdDate, String createdBy, LocalDateTime lastModifiedDate, String lastModifiedBy, Integer version, String name, String code, Integer type) {
        super(id, createdDate, createdBy, lastModifiedDate, lastModifiedBy, version);
        this.name = name;
        this.code = code;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        InvestmentInstruments that = (InvestmentInstruments) o;
        return Objects.equals(getName(), that.getName()) && Objects.equals(getCode(), that.getCode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getCode());
    }
}
