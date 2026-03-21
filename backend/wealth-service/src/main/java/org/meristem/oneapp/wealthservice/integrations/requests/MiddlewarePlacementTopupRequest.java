package org.meristem.oneapp.wealthservice.integrations.requests;

public record MiddlewarePlacementTopupRequest(String placementId,
                                    Double amount,
                                    String customerId) {
}
