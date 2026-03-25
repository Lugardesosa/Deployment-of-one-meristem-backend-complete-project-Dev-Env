package org.meristem.oneapp.coreservices.wealth.domains.responses;

import java.time.LocalDateTime;

public record FundDetailsResponse(String fundId,
                                  String name,
                                  String currency,
                                  Double nav,
                                  LocalDateTime navDate,
                                  String riskLevel) {
}