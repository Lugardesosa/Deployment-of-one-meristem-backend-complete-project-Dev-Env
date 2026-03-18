package org.meristem.oneapp.wealthservice.integrations.responses;

import java.time.ZonedDateTime;

public record MiddlewareRateQuoteResponse(String code,
                                Double rate,
                                ZonedDateTime effectiveDate) {
}
