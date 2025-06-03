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
@Table(name = "virtual_accounts")
public class VirtualAccounts extends BaseModel<String> {

    @Size(min = 2, max = 20)
    @NotBlank(message = "Cannot be blank")
    @Column("bank_code")
    private String bankCode;

    @Size(min = 2, max = 70)
    @NotBlank(message = "Cannot be blank")
    @Column("bank_name")
    private String bankName;

    @Size(min = 8, max = 20)
    @NotBlank(message = "Cannot be blank")
    @Column("account_number")
    private String accountNumber;

    @Size(min = 2, max = 200)
    @NotBlank(message = "Cannot be blank")
    @Column("account_name")
    private String accountName;

    @Size(min = 2, max = 25)
    @NotBlank(message = "Cannot be blank")
    @Column("reference")
    private String reference;

    @Size(min = 8, max = 20)
    @NotBlank(message = "Cannot be blank")
    @Column("provider")
    private String provider;

    @Digits(integer = 20, fraction = 4, message = "Enter a valid decimal number")
    @Column("balance")
    private BigDecimal balance;

    @NotNull(message = "cannot be blank")
    @Column("wallet_id")
    private Long walletId;

    @Builder
    public VirtualAccounts(Long id, LocalDateTime createdDate, String createdBy, LocalDateTime lastModifiedDate, String lastModifiedBy, Integer version,
                           String bankCode, String bankName, String accountNumber, String accountName, String provider, BigDecimal balance, Long walletId,
                           String reference) {
        super(id, createdDate, createdBy, lastModifiedDate, lastModifiedBy, version);
        this.bankCode = bankCode;
        this.bankName = bankName;
        this.accountNumber = accountNumber;
        this.accountName = accountName;
        this.provider = provider;
        this.balance = balance;
        this.walletId = walletId;
        this.reference = reference;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        VirtualAccounts that = (VirtualAccounts) o;
        return Objects.equals(getBankCode(), that.getBankCode()) && Objects.equals(getWalletId(), that.getWalletId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBankCode(), getWalletId());
    }
}
