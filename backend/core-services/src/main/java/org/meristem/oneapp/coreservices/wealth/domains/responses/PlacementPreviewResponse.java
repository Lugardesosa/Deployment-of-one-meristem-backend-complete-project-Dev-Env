package org.meristem.oneapp.coreservices.wealth.domains.responses;

import java.time.LocalDateTime;

public record PlacementPreviewResponse(String productId,
                                       Double amount,
                                       Integer tenorDays,
                                       Double rate,
                                       LocalDateTime maturityDate) {
}
