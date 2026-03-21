package org.meristem.oneapp.wealthservice.integrations.responses;

public record MiddlewarePlacementActionResponse(String referenceId,
                                      String status,
                                      Double amount) {
}
