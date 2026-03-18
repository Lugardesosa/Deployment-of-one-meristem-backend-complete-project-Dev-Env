package org.meristem.oneapp.wealthservice.integrations.responses;

public record MiddlewarePlacementProductResponse(String productId,
                                       String name,
                                       String currency,
                                       Integer tenorDays) {
}
