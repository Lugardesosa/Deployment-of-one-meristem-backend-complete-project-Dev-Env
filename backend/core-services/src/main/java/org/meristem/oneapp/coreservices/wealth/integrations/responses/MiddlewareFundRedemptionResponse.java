package org.meristem.oneapp.coreservices.wealth.integrations.responses;

public record MiddlewareFundRedemptionResponse(String transactionId,
                                     String fundId,
                                     String customerId,
                                     Double amount,
                                     String status) {
}
