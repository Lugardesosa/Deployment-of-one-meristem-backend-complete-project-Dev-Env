package org.meristem.oneapp.coreservices.wealth.integrations.responses;

import java.time.ZonedDateTime;

public record MiddlewareFundHistoryResponse(String fundId,
                                  ZonedDateTime date,
                                  Double nav) {
}
