package org.meristem.oneapp.coreservices.wealth.domains.responses;

import java.time.LocalDateTime;

public record PlacementSummaryResponse(String fundAccountId,
                                       String customerId,
                                       String productId,
                                       Double principal,
                                       LocalDateTime maturityDate) {
}
