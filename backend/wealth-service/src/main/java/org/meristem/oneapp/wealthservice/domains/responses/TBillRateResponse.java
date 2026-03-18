package org.meristem.oneapp.wealthservice.domains.responses;

import java.time.LocalDateTime;

public record TBillRateResponse(String instrumentId,
                                Double rate,
                                LocalDateTime effectiveDate) {
}
