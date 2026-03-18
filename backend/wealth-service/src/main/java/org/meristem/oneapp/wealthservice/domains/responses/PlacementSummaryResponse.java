package org.meristem.oneapp.wealthservice.domains.responses;

import java.time.LocalDateTime;

public record PlacementSummaryResponse(String fundAccountId,
                                       String customerId,
                                       String productId,
                                       Double principal,
                                       LocalDateTime maturityDate) {
}
