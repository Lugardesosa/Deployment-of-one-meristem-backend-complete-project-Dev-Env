package org.meristem.oneapp.wealthservice.integrations.requests;

public record MiddlewareFundRedemptionRequest(String customerId,
                                    Double amount,
                                    String destinationAccountNo) {
}
