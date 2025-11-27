package org.meristem.oneapp.usersservice.models;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.Objects;

@NoArgsConstructor
@Table("investment_options")
@Getter
@Setter
public class InvestmentOptions extends BaseModel<String> {

    private Long investmentId;

    private String name;

    @Builder
    public InvestmentOptions(Long id, LocalDateTime createdDate, String createdBy, LocalDateTime lastModifiedDate, String lastModifiedBy, Integer version, Long investmentId, String name) {
        super(id, createdDate, createdBy, lastModifiedDate, lastModifiedBy, version);
        this.investmentId = investmentId;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        InvestmentOptions that = (InvestmentOptions) o;
        return Objects.equals(getInvestmentId(), that.getInvestmentId()) && Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getInvestmentId(), getName());
    }
}
