package org.meristem.oneapp.coreservices.wealth.domains.responses;

public record PlacementProductResponse(String productId,
                                       String name,
                                       String currency,
                                       Integer tenorDays) {
}
