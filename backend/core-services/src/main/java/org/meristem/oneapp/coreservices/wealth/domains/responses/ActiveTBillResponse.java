package org.meristem.oneapp.coreservices.wealth.domains.responses;

import java.time.LocalDateTime;

public record ActiveTBillResponse(String holdingId,
                                  String customerId,
                                  String instrumentId,
                                  Double amount,
                                  LocalDateTime maturityDate) {
}
