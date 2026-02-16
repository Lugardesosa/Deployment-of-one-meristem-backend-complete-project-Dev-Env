package org.meristem.oneapp.usersservice.models;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
@Table("investment_options_accessed")
public class InvestmentOptionsAccessed extends BaseModel<String> {

    private Long userId;
    private Long optionId;
    private Boolean accessed;

    @Builder
    public InvestmentOptionsAccessed(Integer status, Long id, LocalDateTime createdDate, String createdBy, LocalDateTime lastModifiedDate, String lastModifiedBy, Integer version, Long userId, Long optionId) {
        super(id, createdDate, createdBy, lastModifiedDate, lastModifiedBy, version, status);
        this.userId = userId;
        this.optionId = optionId;
        this.accessed = false;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        InvestmentOptionsAccessed that = (InvestmentOptionsAccessed) o;
        return Objects.equals(getUserId(), that.getUserId()) && Objects.equals(getOptionId(), that.getOptionId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserId(), getOptionId());
    }
}
