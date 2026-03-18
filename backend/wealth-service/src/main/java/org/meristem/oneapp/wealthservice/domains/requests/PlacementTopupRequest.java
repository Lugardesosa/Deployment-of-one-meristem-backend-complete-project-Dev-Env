package org.meristem.oneapp.wealthservice.domains.requests;

public record PlacementTopupRequest(String placementId,
                                    Double amount,
                                    String customerId) {
}
