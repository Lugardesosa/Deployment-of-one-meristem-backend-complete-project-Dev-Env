package org.meristem.oneapp.coreservices.wealth.integrations.responses;

import java.time.ZonedDateTime;

public record MiddlewarePlacementTransactionResponse(String fundAccountId,
                                           String transactionId,
                                           String type,
                                           Double amount,
                                           ZonedDateTime date) {
}
