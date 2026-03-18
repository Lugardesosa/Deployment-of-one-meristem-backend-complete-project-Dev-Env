package org.meristem.oneapp.wealthservice.domains.requests;

public record PlacementPreviewRequest(String productId,
                                      Double amount,
                                      Integer tenorDays,
                                      String customerId) {
}
