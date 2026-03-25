package org.meristem.oneapp.coreservices.wealth.domains.responses;

import java.time.LocalDateTime;

public record FundHistoryResponse(String fundId,
                                  LocalDateTime date,
                                  Double nav) {
}
