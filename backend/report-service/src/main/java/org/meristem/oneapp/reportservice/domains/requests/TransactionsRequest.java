package org.meristem.oneapp.reportservice.domains.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.meristem.oneapp.reportservice.domains.enums.TransactionStatus;
import org.meristem.oneapp.reportservice.domains.enums.TransactionType;

import java.time.OffsetDateTime;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
@Schema(description = "Request object for filtering transaction records")
public class TransactionsRequest extends PageRequest {

    @Schema(description = "Transaction reference", example = "MER-20250701133207-11-VVUNKL")
    private  String reference;

    @PastOrPresent(message = "Must not be in the past")
    @Schema(description = "Start date for filtering transactions", example = "2025-07-01T14:06:01.365Z")
    @Builder.Default
    private OffsetDateTime from = OffsetDateTime.now().minusMonths(2);

    @PastOrPresent(message = "Must not be in the past")
    @Builder.Default
    @Schema(description = "End date for filtering transactions", example = "2025-07-01T14:06:01.365Z")
    private OffsetDateTime to = OffsetDateTime.now();

    @Schema(anyOf = {TransactionType.class}, description = "Type of transaction (e.g., deposit, withdrawal)", example = "1")
    @Builder.Default
    private Integer transactionType = TransactionType.DEPOSIT.getValue();

    @Schema(anyOf = {TransactionStatus.class}, description = "Status of the transaction (e.g., completed, pending)", example = "1")
    @Builder.Default
    private Integer transactionStatus = TransactionStatus.COMPLETED.getValue();
}
