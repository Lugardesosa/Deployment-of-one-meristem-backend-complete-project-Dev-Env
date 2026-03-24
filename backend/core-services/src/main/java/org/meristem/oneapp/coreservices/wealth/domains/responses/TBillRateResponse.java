package org.meristem.oneapp.coreservices.wealth.domains.responses;

import java.time.LocalDateTime;

public record TBillRateResponse(String instrumentId,
                                Double rate,
                                LocalDateTime effectiveDate) {
}
