package org.meristem.oneapp.coreservices.wealth.integrations.responses;

import java.time.ZonedDateTime;

public record MiddlewareFundDetailsResponse(String fundId,
                                  String name,
                                  String currency,
                                  Double nav,
                                  ZonedDateTime navDate,
                                  String riskLevel) {
}