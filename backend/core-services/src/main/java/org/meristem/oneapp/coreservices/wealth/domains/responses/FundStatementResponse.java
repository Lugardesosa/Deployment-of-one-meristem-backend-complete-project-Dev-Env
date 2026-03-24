package org.meristem.oneapp.coreservices.wealth.domains.responses;

import java.time.LocalDateTime;

public record FundStatementResponse(String statementId,
                                    String customerId,
                                    String period,
                                    LocalDateTime generatedOn) {
}
