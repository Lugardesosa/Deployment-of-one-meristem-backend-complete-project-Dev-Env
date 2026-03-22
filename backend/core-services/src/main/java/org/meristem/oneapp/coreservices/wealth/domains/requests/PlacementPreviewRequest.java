package org.meristem.oneapp.coreservices.wealth.domains.requests;

public record PlacementPreviewRequest(String productId,
                                      Double amount,
                                      Integer tenorDays,
                                      String customerId) {
}
