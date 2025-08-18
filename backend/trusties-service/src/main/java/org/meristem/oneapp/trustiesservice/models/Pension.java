package org.meristem.oneapp.trustiesservice.models;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
@Table("pension")
public class Pension extends Assets {

    @NotBlank(message = "Cannot be blank")
    private String pfa;

    @NotBlank(message = "Cannot be blank")
    private String rsa;

    @Builder
    public Pension(Long id, LocalDateTime createdDate, String createdBy, LocalDateTime lastModifiedDate, String lastModifiedBy, Integer version, Long ownerId, BigDecimal estimatedAmount, Long currencyId, String otherDetails, String pfa, String rsa) {
        super(id, createdDate, createdBy, lastModifiedDate, lastModifiedBy, version, ownerId, estimatedAmount, currencyId, otherDetails);
        this.pfa = pfa;
        this.rsa = rsa;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Pension pension = (Pension) o;
        return Objects.equals(getPfa(), pension.getPfa()) && Objects.equals(getRsa(), pension.getRsa());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPfa(), getRsa());
    }
}
