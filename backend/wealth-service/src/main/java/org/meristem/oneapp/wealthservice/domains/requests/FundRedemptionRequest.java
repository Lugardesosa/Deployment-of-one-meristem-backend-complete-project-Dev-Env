package org.meristem.oneapp.wealthservice.domains.requests;

public record FundRedemptionRequest(String customerId,
                                    Double amount,
                                    String destinationAccountNo) {
}
