package org.meristem.oneapp.wealthservice.domains.responses;

import java.time.LocalDateTime;

public record PlacementRateResponse(String productId,
                                    Double rate,
                                    LocalDateTime effectiveDate) {
}
