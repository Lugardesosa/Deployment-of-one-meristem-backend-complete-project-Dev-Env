package org.meristem.oneapp.coreservice.models;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
@Table("life_insurance")
public class LifeInsurance extends Assets {

    @NotBlank(message = "Cannot be blank")
    private String insuranceCompany;

    @NotBlank(message = "Cannot be blank")
    private String policyNumber;

    @NotNull(message = "Cannot be null")
    private LocalDate expiryDate;

    @Builder
    public LifeInsurance(Long id, LocalDateTime createdDate, String createdBy, LocalDateTime lastModifiedDate, String lastModifiedBy, Integer version, Long ownerId, BigDecimal estimatedAmount, Long currencyId, String otherDetails, String insuranceCompany, String policyNumber, LocalDate expiryDate) {
        super(id, createdDate, createdBy, lastModifiedDate, lastModifiedBy, version, ownerId, estimatedAmount, currencyId, otherDetails);
        this.insuranceCompany = insuranceCompany;
        this.policyNumber = policyNumber;
        this.expiryDate = expiryDate;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        LifeInsurance that = (LifeInsurance) o;
        return Objects.equals(getPolicyNumber(), that.getPolicyNumber()) && Objects.equals(getExpiryDate(), that.getExpiryDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPolicyNumber(), getExpiryDate());
    }
}
