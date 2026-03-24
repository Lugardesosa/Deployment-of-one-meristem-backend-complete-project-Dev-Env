package org.meristem.oneapp.coreservices.wealth.integrations.responses;

import java.time.ZonedDateTime;

public record MiddlewareTBillPreviewResponse(String instrumentId,
                                   Double amount,
                                   Integer tenorDays,
                                   Double rate,
                                   ZonedDateTime maturityDate) {
}
