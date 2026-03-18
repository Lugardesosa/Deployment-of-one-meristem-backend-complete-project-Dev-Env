package org.meristem.oneapp.walletservice.domains.responses;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Schema(name = "WalletTransactionResponse", description = "Wallet transaction item returned by middleware.")
public record TransactionResponse(
        @Schema(example = "0001234567")
        String accountNo,
        @Schema(example = "TXN-00000001")
        String transactionId,
        @Schema(example = "DEBIT")
        String type,
        @Schema(example = "5000.00")
        BigDecimal amount,
        @Schema(example = "2026-02-23T08:30:00Z")
        OffsetDateTime date) {
}
