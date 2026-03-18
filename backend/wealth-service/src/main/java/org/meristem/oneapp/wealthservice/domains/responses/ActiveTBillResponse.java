package org.meristem.oneapp.wealthservice.domains.responses;

import java.time.LocalDateTime;

public record ActiveTBillResponse(String holdingId,
                                  String customerId,
                                  String instrumentId,
                                  Double amount,
                                  LocalDateTime maturityDate) {
}
