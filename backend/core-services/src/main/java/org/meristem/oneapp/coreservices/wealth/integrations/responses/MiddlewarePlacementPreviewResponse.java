package org.meristem.oneapp.coreservices.wealth.integrations.responses;

import java.time.ZonedDateTime;

public record MiddlewarePlacementPreviewResponse(String productId,
                                       Double amount,
                                       Integer tenorDays,
                                       Double rate,
                                       ZonedDateTime maturityDate) {
}
