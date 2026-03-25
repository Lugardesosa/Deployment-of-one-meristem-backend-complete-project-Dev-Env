package org.meristem.oneapp.coreservices.wealth.domains.responses;

import java.time.LocalDateTime;

public record TBillPreviewResponse(String instrumentId,
                                   Double amount,
                                   Integer tenorDays,
                                   Double rate,
                                   LocalDateTime maturityDate) {
}
