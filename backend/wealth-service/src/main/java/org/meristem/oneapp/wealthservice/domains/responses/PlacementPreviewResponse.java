package org.meristem.oneapp.wealthservice.domains.responses;

import java.time.LocalDateTime;

public record PlacementPreviewResponse(String productId,
                                       Double amount,
                                       Integer tenorDays,
                                       Double rate,
                                       LocalDateTime maturityDate) {
}
