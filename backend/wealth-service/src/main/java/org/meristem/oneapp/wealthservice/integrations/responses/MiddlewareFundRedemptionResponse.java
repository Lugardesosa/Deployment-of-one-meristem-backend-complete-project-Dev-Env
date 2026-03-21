package org.meristem.oneapp.wealthservice.integrations.responses;

public record MiddlewareFundRedemptionResponse(String transactionId,
                                     String fundId,
                                     String customerId,
                                     Double amount,
                                     String status) {
}
