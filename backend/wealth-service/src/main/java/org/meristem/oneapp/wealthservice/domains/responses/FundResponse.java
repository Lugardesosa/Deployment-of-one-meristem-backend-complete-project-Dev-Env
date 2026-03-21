package org.meristem.oneapp.wealthservice.domains.responses;

import java.time.LocalDateTime;

public record FundResponse(String fundId,
                           String name,
                           String currency,
                           Double nav,
                           LocalDateTime navDate) {
}
