package org.meristem.oneapp.wealthservice.integrations.requests;

public record MiddlewarePlacementPreviewRequest(
        String fundId,
        String productId,
        String effectiveDate,
        Double amount,
        Integer tenorDays
) {}