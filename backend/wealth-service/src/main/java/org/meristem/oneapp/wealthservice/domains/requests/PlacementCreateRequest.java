package org.meristem.oneapp.wealthservice.domains.requests;

public record PlacementCreateRequest(String productId,
                                     Double amount,
                                     Integer tenorDays,
                                     String customerId) {
}
