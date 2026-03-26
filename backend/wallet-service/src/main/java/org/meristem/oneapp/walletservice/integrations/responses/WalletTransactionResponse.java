package org.meristem.oneapp.walletservice.integrations.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Schema(name = "WalletTransactionResponse", description = "Wallet transaction item returned by middleware.")
public record WalletTransactionResponse(
        @JsonProperty("account_name")
        String accountName,

        @JsonProperty("account_no")
        String accountNo,

        @JsonProperty("alternate_account_no")
        String alternateAccountNo,

        @JsonProperty("business_date")
        LocalDateTime businessDate,

        @JsonProperty("cheque_reference")
        String chequeReference,

        @JsonProperty("currency_id")
        String currencyId,

        @JsonProperty("currency_name")
        String currencyName,

        @JsonProperty("customer_id")
        String customerId,

        @JsonProperty("effective_date")
        LocalDateTime effectiveDate,

        @JsonProperty("is_reversed_yesno")
        String isReversedYesno,

        @JsonProperty("nuban_account_no")
        String nubanAccountNo,

        @JsonProperty("processing_date")
        LocalDateTime processingDate,

        @JsonProperty("transaction_amount")
        BigDecimal transactionAmount,

        @JsonProperty("transaction_description")
        String transactionDescription,

        @JsonProperty("transaction_number")
        Integer transactionNumber,

        @JsonProperty("transaction_reference")
        String transactionReference,

        @JsonProperty("transaction_type")
        String transactionType
) {
}
