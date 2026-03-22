package org.meristem.oneapp.coreservices.wealth.domains.responses;

public record FundSubscriptionResponse(String transactionId,
                                       String fundId,
                                       String customerId,
                                       Double amount,
                                       String status) {
}
