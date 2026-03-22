package org.meristem.oneapp.coreservices.wealth.domains.responses;

import java.time.LocalDateTime;

public record PlacementTransactionResponse(String fundAccountId,
                                           String transactionId,
                                           String type,
                                           Double amount,
                                           LocalDateTime date) {
}
