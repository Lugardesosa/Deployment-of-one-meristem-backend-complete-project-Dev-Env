package org.meristem.oneapp.wealthservice.domains.requests;

public record FundSubscriptionRequest(
        Double amount,
        String accountName
) {}