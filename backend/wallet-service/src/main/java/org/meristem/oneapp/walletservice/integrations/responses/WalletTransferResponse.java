package org.meristem.oneapp.walletservice.integrations.responses;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(name = "WalletTransferResponse", description = "Transfer details returned by middleware wallet transfer endpoint.")
public record WalletTransferResponse(
        @Schema(example = "TRF-00001234")
        String transferId,
        @Schema(example = "0001234567")
        String fromAccountNo,
        @Schema(example = "0007654321")
        String toAccountNo,
        @Schema(example = "15000.50")
        BigDecimal amount,
        @Schema(example = "SUCCESS")
        String status) {
}
