package org.meristem.oneapp.wealthservice.integrations.requests;

public record MiddlewarePlacementLiquidateRequest(
        String placementId,
        String reference,
        String date
) {}