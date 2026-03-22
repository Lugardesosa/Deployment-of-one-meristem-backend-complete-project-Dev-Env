package org.meristem.oneapp.coreservices.wealth.integrations.responses;

import java.time.ZonedDateTime;

public record MiddlewareRateQuoteResponse(String code,
                                Double rate,
                                ZonedDateTime effectiveDate) {
}
