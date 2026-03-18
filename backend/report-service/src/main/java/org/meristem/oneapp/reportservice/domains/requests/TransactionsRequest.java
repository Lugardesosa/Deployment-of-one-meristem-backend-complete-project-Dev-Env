package org.meristem.oneapp.reportservice.domains.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.meristem.oneapp.reportservice.domains.enums.TransactionStatus;
import org.meristem.oneapp.reportservice.domains.enums.TransactionSubject;
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
    private OffsetDateTime from;

    @PastOrPresent(message = "Must not be in the past")
    @Schema(description = "End date for filtering transactions", example = "2025-07-01T14:06:01.365Z")
    private OffsetDateTime to;

    @Schema(anyOf = {TransactionType.class}, description = "Type of transaction (e.g., deposit, withdrawal)", example = "1")
    private TransactionType transactionType;

    @Schema(anyOf = {TransactionStatus.class}, description = "Status of the transaction (e.g., completed, pending)", example = "1")
    private TransactionStatus transactionStatus;

    private TransactionSubject transactionSubject;
}
