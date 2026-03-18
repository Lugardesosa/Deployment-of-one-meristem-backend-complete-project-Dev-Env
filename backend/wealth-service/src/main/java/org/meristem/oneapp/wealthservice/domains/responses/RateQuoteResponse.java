package org.meristem.oneapp.wealthservice.domains.responses;

import java.time.LocalDateTime;

public record RateQuoteResponse(String code,
                                Double rate,
                                LocalDateTime effectiveDate) {
}
