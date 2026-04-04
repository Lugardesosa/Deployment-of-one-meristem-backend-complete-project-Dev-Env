package org.meristem.oneapp.wealthservice.domains.requests;

public record FixedDepositPreviewRequest(
        String fundId,
        String productId,
        String effectiveDate,
        Double amount,
        Integer tenorDays,
        String customerId,
        String reInvestmentPercentage
) {}