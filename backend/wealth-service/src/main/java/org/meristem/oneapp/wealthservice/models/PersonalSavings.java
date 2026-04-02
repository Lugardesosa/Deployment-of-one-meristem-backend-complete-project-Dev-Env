package org.meristem.oneapp.wealthservice.models;

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
@Table("personal_savings")
public class PersonalSavings extends BaseModel<String> {

    private String name;
    private String identifier;
    private String description;

    private Integer interestRate;
    private Integer tax;

    private Boolean fundActivePlan;
    private Boolean lockedTillMaturity;

    private Integer minimumDurationDays;
    private Integer maximumDurationDays;

    private Long minimumTargetKobo;
    private Long maximumTargetKobo;

    private Long minimumActiveSavingFundingAmount;
    private Long maximumActiveSavingFundingAmount;

    @Builder
    public PersonalSavings(Long id, LocalDateTime createdDate, String createdBy, LocalDateTime lastModifiedDate, String lastModifiedBy, Integer version, Integer status, String name, String identifier, String description, Integer interestRate, Integer tax, Boolean fundActivePlan, Boolean lockedTillMaturity, Integer minimumDurationDays, Integer maximumDurationDays, Long minimumTargetKobo, Long maximumTargetKobo, Long minimumActiveSavingFundingAmount, Long maximumActiveSavingFundingAmount) {
        super(id, createdDate, createdBy, lastModifiedDate, lastModifiedBy, version, status);
        this.name = name;
        this.identifier = identifier;
        this.description = description;
        this.interestRate = interestRate;
        this.tax = tax;
        this.fundActivePlan = fundActivePlan;
        this.lockedTillMaturity = lockedTillMaturity;
        this.minimumDurationDays = minimumDurationDays;
        this.maximumDurationDays = maximumDurationDays;
        this.minimumTargetKobo = minimumTargetKobo;
        this.maximumTargetKobo = maximumTargetKobo;
        this.minimumActiveSavingFundingAmount = minimumActiveSavingFundingAmount;
        this.maximumActiveSavingFundingAmount = maximumActiveSavingFundingAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PersonalSavings personalSavings = (PersonalSavings) o;
        return Objects.equals(getId(), personalSavings.getId()) && Objects.equals(getIdentifier(), personalSavings.getIdentifier());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getIdentifier());
    }
}