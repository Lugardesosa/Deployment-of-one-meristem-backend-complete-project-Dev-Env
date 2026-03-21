package org.meristem.oneapp.wealthservice.integrations.requests;

public record MiddlewarePlacementPreviewRequest(String productId,
                                      Double amount,
                                      Integer tenorDays,
                                      String customerId) {
}
