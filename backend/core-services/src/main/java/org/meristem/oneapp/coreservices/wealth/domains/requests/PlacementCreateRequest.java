package org.meristem.oneapp.coreservices.wealth.domains.requests;

public record PlacementCreateRequest(String productId,
                                     Double amount,
                                     Integer tenorDays,
                                     String customerId) {
}
