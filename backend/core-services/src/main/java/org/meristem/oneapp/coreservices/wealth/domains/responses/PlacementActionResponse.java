package org.meristem.oneapp.coreservices.wealth.domains.responses;

public record PlacementActionResponse(String referenceId,
                                      String status,
                                      Double amount) {
}
