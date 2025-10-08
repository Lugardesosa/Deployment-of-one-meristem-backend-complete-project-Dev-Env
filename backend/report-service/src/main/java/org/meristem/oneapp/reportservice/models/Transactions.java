package org.meristem.oneapp.reportservice.models;


import jakarta.validation.constraints.NotNull;
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
@Getter
@Setter
@Table("transactions")
public class Transactions extends BaseModel<String> {

    @NotNull(message = "Cannot be null")
    @Column("wallet_id")
    private Long walletId;

    @Column("virtual_account_id")
    private Long virtualAccountId;

    @NotNull(message = "Cannot be null")
    @Size(max = 50)
    @Column("reference")
    private String reference;

    @NotNull(message = "Cannot be null")
    @Size(max = 50)
    @Column("provider_reference")
    private String providerReference;

    @NotNull(message = "Cannot be null")
    @Column("amount")
    private BigDecimal amount;

    @Column("previous_balance")
    private BigDecimal previousBalance;

    @Column("new_balance")
    private BigDecimal newBalance;

    @NotNull(message = "Cannot be null")
    @Column("type")
    private String type;

    @NotNull(message = "Cannot be null")
    @Column("method")
    private String method;

    @Column("narration")
    private String narration;

    @Column("senders_bank_name")
    private String sendersBankName;

    @Column("senders_bank_code")
    private String sendersBankCode;

    @Column("senders_account_number")
    private String sendersAccountNumber;

    @Column("senders_name")
    private String sendersName;

    @Column("account_number")
    private String accountNumber;

    @Column("account_name")
    private String accountName;

    @Column("transaction_date")
    private LocalDateTime transactionDate;

    @NotNull(message = "Cannot be null")
    @Column("transaction_status")
    private Integer transactionStatus;

    @Column("provider_transaction_date")
    private LocalDateTime providerTransactionDate;

    @Builder
    public Transactions(Long id, LocalDateTime createdDate, String createdBy, LocalDateTime lastModifiedDate, String lastModifiedBy, Integer version,
                        Long walletId, Long virtualAccountId, String reference, BigDecimal amount, BigDecimal previousBalance, BigDecimal newBalance, String type,
                        String method, String narration, String providerReference, String sendersBankName, String sendersBankCode,
                        String sendersName, String sendersAccountNumber, String accountNumber, String accountName, LocalDateTime transactionDate, LocalDateTime providerTransactionDate,
                        Integer transactionStatus) {
        super(id, createdDate, createdBy, lastModifiedDate, lastModifiedBy, version);
        this.walletId = walletId;
        this.virtualAccountId = virtualAccountId;
        this.reference = reference;
        this.providerReference = providerReference;
        this.amount = amount;
        this.previousBalance = previousBalance;
        this.newBalance = newBalance;
        this.type = type;
        this.method = method;
        this.narration = narration;
        this.sendersBankName = sendersBankName;
        this.sendersBankCode = sendersBankCode;
        this.sendersName = sendersName;
        this.sendersAccountNumber = sendersAccountNumber;
        this.accountNumber = accountNumber;
        this.accountName = accountName;
        this.transactionDate = transactionDate;
        this.providerTransactionDate = providerTransactionDate;
        this.transactionStatus = transactionStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Transactions that = (Transactions) o;
        return Objects.equals(getReference(), that.getReference());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getReference());
    }
}
