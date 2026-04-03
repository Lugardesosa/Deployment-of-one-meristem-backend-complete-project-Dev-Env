package org.meristem.oneapp.wealthservice.integrations.responses;

public record MiddlewareFixedDepositTransactionResponse(
        String fundId,
        String fundDescription,
        String productId,
        String productDescription,
        String currencyId,
        String currencyDescription,
        String customerId,
        String customerName,
        String fundAccountId,
        String effectiveDate,
        String transactionDate,
        String transactionDescription,
        String transactionReference,
        Double transactionAmount,
        String transactionType
) {}