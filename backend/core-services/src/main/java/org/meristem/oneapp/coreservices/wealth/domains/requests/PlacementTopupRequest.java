package org.meristem.oneapp.coreservices.wealth.domains.requests;

public record PlacementTopupRequest(String placementId,
                                    Double amount,
                                    String customerId) {
}
