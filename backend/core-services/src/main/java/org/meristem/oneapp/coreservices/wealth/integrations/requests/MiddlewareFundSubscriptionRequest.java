package org.meristem.oneapp.coreservices.wealth.integrations.requests;

public record MiddlewareFundSubscriptionRequest(String customerId,
                                      Double amount,
                                      String sourceAccountNo) {
}
