package org.meristem.oneapp.wealthservice.integrations.responses;

public record MiddlewareInvestmentProductListResponse(
        String status,
        String message,
        MiddlewareInvestmentProductDataResponse data
) {}