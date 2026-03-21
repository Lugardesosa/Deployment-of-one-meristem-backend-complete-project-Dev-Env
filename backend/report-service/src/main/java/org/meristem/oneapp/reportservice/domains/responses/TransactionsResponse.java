package org.meristem.oneapp.reportservice.domains.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Builder
@Schema(description = "Response object for transaction details")
public record TransactionsResponse(
        @Schema(description = "Unique identifier for the wallet") Long walletId,
        @Schema(description = "Unique identifier for the virtual account") Long virtualAccountId,
        @Schema(description = "Transaction reference") String reference,
        @Schema(description = "Provider's transaction reference") String providerReference,
        @Schema(description = "Transaction amount") BigDecimal amount,
        @Schema(description = "Previous balance before the transaction") BigDecimal previousBalance,
        @Schema(description = "New balance after the transaction") BigDecimal newBalance,
        @Schema(description = "Type of transaction (e.g., debit, credit)") String type,
        @Schema(description = "Transaction method (e.g., bank transfer, card payment)") String method,
        @Schema(description = "Narration or description of the transaction") String narration,
        @Schema(description = "Additional metadata for the transaction") Map<String, Object> metadata,
        @Schema(description = "Sender's bank name") String sendersBankName,
        @Schema(description = "Sender's bank code") String sendersBankCode,
        @Schema(description = "Sender's account number") String sendersAccountNumber,
        @Schema(description = "Sender's name") String sendersName,
        @Schema(description = "Transaction status code") Integer status,
        @Schema(description = "Recipient's account number") String accountNumber,
        @Schema(description = "Recipient's account name") String accountName,
        @Schema(description = "Date we received the transactions") LocalDateTime transactionDate,
        @Schema(description = "Status of the transactions") Integer transactionStatus,
        @Schema(description = "Date provider received the transactions") LocalDateTime providerTransactionDate
) {
}
