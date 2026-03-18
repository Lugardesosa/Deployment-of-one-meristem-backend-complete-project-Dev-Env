package org.meristem.oneapp.wealthservice.integrations.requests;

public record MiddlewareFundSubscriptionRequest(String customerId,
                                      Double amount,
                                      String sourceAccountNo) {
}
