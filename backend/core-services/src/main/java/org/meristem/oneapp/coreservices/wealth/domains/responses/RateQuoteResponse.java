package org.meristem.oneapp.coreservices.wealth.domains.responses;

import java.time.LocalDateTime;

public record RateQuoteResponse(String code,
                                Double rate,
                                LocalDateTime effectiveDate) {
}
