package org.meristem.oneapp.reportservice.domains.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;
import org.meristem.oneapp.reportservice.domains.enums.TransactionStatus;
import org.meristem.oneapp.reportservice.domains.enums.TransactionType;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Schema(description = "Request object for filtering transaction records")
public class TransactionsRequest extends PageRequest {

    @Schema(description = "Transaction reference", example = "MER-20250701133207-11-VVUNKL")
    private  String reference;

    @PastOrPresent(message = "Must not be in the past")
    @Schema(description = "Start date for filtering transactions", example = "2025-07-01T14:06:01.365Z")
    @Builder.Default
    private LocalDateTime from = LocalDateTime.now().minusMonths(2);

    @PastOrPresent(message = "Must not be in the past")
    @Builder.Default
    @Schema(description = "End date for filtering transactions", example = "2025-07-01T14:06:01.365Z")
    private LocalDateTime to = LocalDateTime.now();

    @Schema(anyOf = {TransactionType.class}, description = "Type of transaction (e.g., deposit, withdrawal)", example = "1")
    @Builder.Default
    private Integer transactionType = TransactionType.DEPOSIT.getValue();

    @Schema(anyOf = {TransactionStatus.class}, description = "Status of the transaction (e.g., completed, pending)", example = "1")
    @Builder.Default
    private Integer transactionStatus = TransactionStatus.COMPLETED.getValue();
}
