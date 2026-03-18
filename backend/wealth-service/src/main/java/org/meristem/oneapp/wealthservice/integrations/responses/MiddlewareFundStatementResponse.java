package org.meristem.oneapp.wealthservice.integrations.responses;

import java.time.ZonedDateTime;

public record MiddlewareFundStatementResponse(String statementId,
                                    String customerId,
                                    String period,
                                    ZonedDateTime generatedOn) {
}
