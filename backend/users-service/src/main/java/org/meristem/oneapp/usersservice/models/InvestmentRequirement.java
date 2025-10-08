package org.meristem.oneapp.usersservice.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * This table is the join table for the investment instrument and requirement tables
 * The requirement instrument houses all the possible requirements stating whether they are mandatory or not
 * The requirement table also tells us what investment instrument the requirement belongs to
 * The investment instrument table houses all the available investment types
 */
@ToString
@NoArgsConstructor
@Setter
@Getter
@Table("investment_requirement")
public class InvestmentRequirement extends BaseModel<String> {

    @Column("investment_id")
    private Long investmentId;

    @Column("requirement_id")
    private Long requirementId;

    public InvestmentRequirement(Long id, LocalDateTime createdDate, String createdBy, LocalDateTime lastModifiedDate, String lastModifiedBy, Integer version, Long investmentId, Long requirementId) {
        super(id, createdDate, createdBy, lastModifiedDate, lastModifiedBy, version);
        this.investmentId = investmentId;
        this.requirementId = requirementId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        InvestmentRequirement that = (InvestmentRequirement) o;
        return Objects.equals(getInvestmentId(), that.getInvestmentId()) && Objects.equals(getRequirementId(), that.getRequirementId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getInvestmentId(), getRequirementId());
    }

}
