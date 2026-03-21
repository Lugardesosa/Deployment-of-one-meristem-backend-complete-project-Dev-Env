package org.meristem.oneapp.wealthservice.domains.responses;

public record PlacementActionResponse(String referenceId,
                                      String status,
                                      Double amount) {
}
