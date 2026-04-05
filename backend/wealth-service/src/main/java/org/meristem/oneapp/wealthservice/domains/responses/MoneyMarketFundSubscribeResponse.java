package org.meristem.oneapp.wealthservice.domains.responses;

public record MoneyMarketFundSubscribeResponse(
        String transactionId,
        String fundId,
        String customerId,
        Double amount,
        String fundAccountNo,
        String status
) {}