package org.meristem.oneapp.coreservices.wealth.integrations.requests;

public record MiddlewarePlacementCreateRequest(String productId,
                                     Double amount,
                                     Integer tenorDays,
                                     String customerId) {
}
