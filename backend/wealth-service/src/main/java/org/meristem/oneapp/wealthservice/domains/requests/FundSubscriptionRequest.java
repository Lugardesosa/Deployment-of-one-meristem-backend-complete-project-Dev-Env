package org.meristem.oneapp.wealthservice.domains.requests;

public record FundSubscriptionRequest(String customerId,
                                      Double amount,
                                      String sourceAccountNo) {
}
