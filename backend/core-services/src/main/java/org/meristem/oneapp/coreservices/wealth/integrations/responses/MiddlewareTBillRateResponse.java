package org.meristem.oneapp.coreservices.wealth.integrations.responses;

import java.time.ZonedDateTime;

public record MiddlewareTBillRateResponse(String instrumentId,
                                          Double rate,
                                          ZonedDateTime effectiveDate) {
}
