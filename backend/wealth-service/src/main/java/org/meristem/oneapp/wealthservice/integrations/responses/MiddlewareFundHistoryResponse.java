package org.meristem.oneapp.wealthservice.integrations.responses;

import java.time.ZonedDateTime;

public record MiddlewareFundHistoryResponse(String fundId,
                                  ZonedDateTime date,
                                  Double nav) {
}
