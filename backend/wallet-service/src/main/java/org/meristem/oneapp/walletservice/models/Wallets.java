package org.meristem.oneapp.walletservice.models;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@ToString
@NoArgsConstructor
@Setter
@Getter
@Table("wallets")
public class Wallets extends BaseModel<String> {

    @Digits(integer = 20, fraction = 4, message = "Enter a valid decimal number")
    @Column("balance")
    private BigDecimal balance;

    @NotNull(message = "cannot be blank")
    @Column("user_id")
    private Long userId;

    @Size(min = 2, max = 200)
    @NotBlank(message = "Cannot be blank")
    @Column("full_name")
    private String fullName;

    @Builder
    public Wallets(Long id, LocalDateTime createdDate, String createdBy, LocalDateTime lastModifiedDate, String lastModifiedBy, Integer version,
                   BigDecimal balance, Long userId, String fullName) {
        super(id, createdDate, createdBy, lastModifiedDate, lastModifiedBy, version);
        this.balance = balance;
        this.userId = userId;
        this.fullName = fullName;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Wallets wallets = (Wallets) o;
        return Objects.equals(getUserId(), wallets.getUserId()) && Objects.equals(getFullName(), wallets.getFullName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserId(), getFullName());
    }
}
