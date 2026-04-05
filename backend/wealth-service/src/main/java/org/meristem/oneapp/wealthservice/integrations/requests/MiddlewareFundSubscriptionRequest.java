package org.meristem.oneapp.wealthservice.integrations.requests;

public record MiddlewareFundSubscriptionRequest(
        String customerId,
        String accountName,
        Double amount
) {}