package org.meristem.oneapp.wealthservice.domains.responses;

public record FundRedemptionResponse(String transactionId,
                                     String fundId,
                                     String customerId,
                                     Double amount,
                                     String status) {
}
