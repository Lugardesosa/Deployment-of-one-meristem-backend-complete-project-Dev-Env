package org.meristem.oneapp.coreservices.wealth.integrations.responses;

public record MiddlewarePlacementProductResponse(String productId,
                                       String name,
                                       String currency,
                                       Integer tenorDays) {
}
