package org.meristem.oneapp.coreservices.wealth.integrations.responses;

import java.time.ZonedDateTime;

public record MiddlewareActiveTBillResponse(String holdingId,
                                  String customerId,
                                  String instrumentId,
                                  Double amount,
                                  ZonedDateTime maturityDate) {
}
