package org.meristem.oneapp.wealthservice.domains.responses;

import java.time.LocalDateTime;

public record PlacementTransactionResponse(String fundAccountId,
                                           String transactionId,
                                           String type,
                                           Double amount,
                                           LocalDateTime date) {
}
