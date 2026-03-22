package org.meristem.oneapp.coreservices.wealth.integrations.responses;

import java.time.ZonedDateTime;

public record MiddlewareFundStatementResponse(String statementId,
                                    String customerId,
                                    String period,
                                    ZonedDateTime generatedOn) {
}
