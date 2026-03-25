package org.meristem.oneapp.coreservices.wealth.integrations.responses;

import java.time.ZonedDateTime;

public record MiddlewarePlacementSummaryResponse(String fundAccountId,
                                       String customerId,
                                       String productId,
                                       Double principal,
                                       ZonedDateTime maturityDate) {
}
