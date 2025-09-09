package org.meristem.oneapp.usersservice.models;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.Objects;

@NoArgsConstructor
@Setter
@Getter
@Table("risk_appetite")
public class RiskAppetite extends BaseModel<String> {

    private Long userId;

    private String riskType;

    private String riskAppetiteDescription;

    private String riskProfile;

    @Builder
    public RiskAppetite(Long id, LocalDateTime createdDate, String createdBy, LocalDateTime lastModifiedDate, String lastModifiedBy, Integer version, Long userId, String riskType, String riskAppetiteDescription,
                        String riskProfile) {
        super(id, createdDate, createdBy, lastModifiedDate, lastModifiedBy, version);
        this.userId = userId;
        this.riskType = riskType;
        this.riskAppetiteDescription = riskAppetiteDescription;
        this.riskProfile = riskProfile;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RiskAppetite that = (RiskAppetite) o;
        return Objects.equals(getUserId(), that.getUserId()) && Objects.equals(getRiskType(), that.getRiskType());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserId(), getRiskType());
    }
}
