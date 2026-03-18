package org.meristem.oneapp.wealthservice.integrations.responses;

import java.time.ZonedDateTime;

public record MiddlewarePlacementSummaryResponse(String fundAccountId,
                                       String customerId,
                                       String productId,
                                       Double principal,
                                       ZonedDateTime maturityDate) {
}
