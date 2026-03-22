package org.meristem.oneapp.coreservices.wealth.integrations.responses;

import java.time.ZonedDateTime;

public record MiddlewareFundResponse(String fundId,
                           String name,
                           String currency,
                           Double nav,
                           ZonedDateTime navDate) {
}
