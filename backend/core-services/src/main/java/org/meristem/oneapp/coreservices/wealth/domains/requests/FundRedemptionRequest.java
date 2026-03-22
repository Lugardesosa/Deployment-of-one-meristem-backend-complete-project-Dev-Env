package org.meristem.oneapp.coreservices.wealth.domains.requests;

public record FundRedemptionRequest(String customerId,
                                    Double amount,
                                    String destinationAccountNo) {
}
