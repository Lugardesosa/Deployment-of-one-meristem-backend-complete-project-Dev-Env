package org.meristem.oneapp.coreservices.wealth.integrations.responses;

import java.time.ZonedDateTime;

public record MiddlewarePlacementRateResponse(String productId,
                                    Double rate,
                                    ZonedDateTime effectiveDate) {
}
