package org.meristem.oneapp.walletservice.integrations.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Schema(name = "WalletTransferRequest", description = "Request body for wallet-to-wallet transfer.")
public record WalletTransferRequest(
        @Schema(description = "Debited wallet account number.", example = "0001234567")
        @NotBlank(message = "fromAccountNo cannot be blank")
        String fromAccountNo,
        @Schema(description = "Credited wallet account number.", example = "0007654321")
        @NotBlank(message = "toAccountNo cannot be blank")
        String toAccountNo,
        @Schema(description = "Amount to transfer.", example = "15000.50")
        @NotNull(message = "amount cannot be null")
        @DecimalMin(value = "0.01", message = "amount must be greater than zero")
        BigDecimal amount,
        @Schema(description = "Transfer narration.", example = "Wallet transfer for savings")
        @NotBlank(message = "narration cannot be blank")
        String narration) {
}
