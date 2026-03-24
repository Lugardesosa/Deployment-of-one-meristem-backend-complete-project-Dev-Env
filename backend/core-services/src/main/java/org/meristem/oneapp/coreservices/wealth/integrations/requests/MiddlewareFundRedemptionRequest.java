package org.meristem.oneapp.coreservices.wealth.integrations.requests;

public record MiddlewareFundRedemptionRequest(String customerId,
                                    Double amount,
                                    String destinationAccountNo) {
}
