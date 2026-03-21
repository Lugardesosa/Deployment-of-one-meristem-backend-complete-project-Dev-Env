package org.meristem.oneapp.wealthservice.domains.responses;

import java.time.LocalDateTime;

public record FundStatementResponse(String statementId,
                                    String customerId,
                                    String period,
                                    LocalDateTime generatedOn) {
}
