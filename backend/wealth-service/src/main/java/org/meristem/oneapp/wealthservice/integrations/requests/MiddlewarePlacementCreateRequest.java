package org.meristem.oneapp.wealthservice.integrations.requests;

public record MiddlewarePlacementCreateRequest(String productId,
                                     Double amount,
                                     Integer tenorDays,
                                     String customerId) {
}
