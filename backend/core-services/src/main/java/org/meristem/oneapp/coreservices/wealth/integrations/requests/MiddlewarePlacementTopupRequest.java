package org.meristem.oneapp.coreservices.wealth.integrations.requests;

public record MiddlewarePlacementTopupRequest(String placementId,
                                    Double amount,
                                    String customerId) {
}
