package org.meristem.oneapp.wealthservice.integrations.responses;

import java.time.ZonedDateTime;

public record MiddlewareTBillRateResponse(String instrumentId,
                                          Double rate,
                                          ZonedDateTime effectiveDate) {
}
