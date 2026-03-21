package org.meristem.oneapp.wealthservice.integrations.responses;

import java.time.ZonedDateTime;

public record MiddlewarePlacementRateResponse(String productId,
                                    Double rate,
                                    ZonedDateTime effectiveDate) {
}
