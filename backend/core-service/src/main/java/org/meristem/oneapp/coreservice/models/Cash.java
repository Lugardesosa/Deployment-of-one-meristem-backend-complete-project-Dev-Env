package org.meristem.oneapp.coreservice.models;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@NoArgsConstructor
@Table("cash")
@Setter
@Getter
public class Cash extends Assets {

    @Size(max = 300)
    @NotBlank(message = "Cannot be blank")
    @Column("account_name")
    private String accountName;

    @Size(max = 15)
    @NotBlank(message = "Cannot be blank")
    @Column("account_number")
    private String accountNumber;

    @Size(max = 15)
    @NotBlank(message = "Cannot be blank")
    @Column("account_type")
    private String accountType;

    @Builder
    public Cash(Long id, LocalDateTime createdDate, String createdBy, LocalDateTime lastModifiedDate, String lastModifiedBy, Integer version, Long ownerId, BigDecimal estimatedAmount, Long currencyId, String otherDetails, String accountName, String accountNumber, String accountType) {
        super(id, createdDate, createdBy, lastModifiedDate, lastModifiedBy, version, ownerId, estimatedAmount, currencyId, otherDetails);
        this.accountName = accountName;
        this.accountNumber = accountNumber;
        this.accountType = accountType;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Cash cash = (Cash) o;
        return Objects.equals(getOwnerId(), cash.getOwnerId()) && Objects.equals(getAccountNumber(), cash.getAccountNumber());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOwnerId(), getAccountNumber());
    }
}
