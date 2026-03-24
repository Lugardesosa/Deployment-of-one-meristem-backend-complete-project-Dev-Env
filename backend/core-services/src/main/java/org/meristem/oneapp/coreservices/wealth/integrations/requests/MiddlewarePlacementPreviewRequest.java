package org.meristem.oneapp.coreservices.wealth.integrations.requests;

public record MiddlewarePlacementPreviewRequest(String productId,
                                      Double amount,
                                      Integer tenorDays,
                                      String customerId) {
}
