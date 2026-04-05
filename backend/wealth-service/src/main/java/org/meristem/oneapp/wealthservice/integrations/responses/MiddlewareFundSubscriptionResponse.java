package org.meristem.oneapp.wealthservice.integrations.responses;

public record MiddlewareFundSubscriptionResponse(
        String transactionId,
        String fundId,
        String customerId,
        Double amount,
        String fundAccountNo,
        String status
) {}