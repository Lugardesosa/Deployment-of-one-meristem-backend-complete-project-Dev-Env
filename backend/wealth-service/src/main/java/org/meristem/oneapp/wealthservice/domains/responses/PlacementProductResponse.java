package org.meristem.oneapp.wealthservice.domains.responses;

public record PlacementProductResponse(String productId,
                                       String name,
                                       String currency,
                                       Integer tenorDays) {
}
