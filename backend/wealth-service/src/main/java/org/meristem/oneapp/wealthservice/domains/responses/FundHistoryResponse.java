package org.meristem.oneapp.wealthservice.domains.responses;

import java.time.LocalDateTime;

public record FundHistoryResponse(String fundId,
                                  LocalDateTime date,
                                  Double nav) {
}
