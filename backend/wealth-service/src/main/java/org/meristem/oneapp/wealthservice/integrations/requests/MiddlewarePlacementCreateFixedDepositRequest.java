package org.meristem.oneapp.wealthservice.integrations.requests;

public record MiddlewarePlacementCreateFixedDepositRequest(
        String productId,
        String customerId,
        String symplusAccountNo,
        String date,
        Integer tenorInDays,
        Double amount,
        String rollover,
        String reference
) {}