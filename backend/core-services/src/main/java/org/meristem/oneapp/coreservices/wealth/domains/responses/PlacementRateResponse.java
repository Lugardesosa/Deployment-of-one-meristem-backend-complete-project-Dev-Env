package org.meristem.oneapp.coreservices.wealth.domains.responses;

import java.time.LocalDateTime;

public record PlacementRateResponse(String productId,
                                    Double rate,
                                    LocalDateTime effectiveDate) {
}
