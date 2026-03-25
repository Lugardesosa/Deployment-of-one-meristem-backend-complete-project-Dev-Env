package org.meristem.oneapp.coreservices.wealth.domains.requests;

public record FundSubscriptionRequest(String customerId,
                                      Double amount,
                                      String sourceAccountNo) {
}
