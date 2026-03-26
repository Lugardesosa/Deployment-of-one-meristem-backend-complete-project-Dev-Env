package org.meristem.oneapp.walletservice.domains.responses;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Schema(name = "TransactionRecord")
public record TransactionResponse(

        @Schema(example = "WIZA Darrel Leo")
        String accountName,

        @Schema(example = "NGN0012120001")
        String accountNo,

        @Schema(example = "null", nullable = true)
        String alternateAccountNo,

        @Schema(example = "2025-12-31T00:00:00")
        LocalDateTime businessDate,

        @Schema(example = "null", nullable = true)
        String chequeReference,

        @Schema(example = "NGN")
        String currencyId,

        @Schema(example = "NIGERIAN NAIRA")
        String currencyName,

        @Schema(example = "001212")
        String customerId,

        @Schema(example = "2025-12-31T00:00:00")
        LocalDateTime effectiveDate,

        @Schema(example = "No")
        String isReversedYesno,

        @Schema(example = "null", nullable = true)
        String nubanAccountNo,

        @Schema(example = "2025-12-31T00:00:00")
        LocalDateTime processingDate,

        @Schema(example = "500")
        BigDecimal transactionAmount,

        @Schema(example = "Payment from Monnify Limited via Monnify")
        String transactionDescription,

        @Schema(example = "204")
        Integer transactionNumber,

        @Schema(example = "MNFY|00|20260326151849|000273")
        String transactionReference,

        @Schema(example = "Credit")
        String transactionType
) {}