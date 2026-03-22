package org.meristem.oneapp.coreservices.wealth.integrations.responses;

public record MiddlewareFundSubscriptionResponse(String transactionId,
                                       String fundId,
                                       String customerId,
                                       Double amount,
                                       String status) {
}
