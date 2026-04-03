package org.meristem.oneapp.wealthservice.integrations.responses;

import java.util.List;

public record MiddlewareFixedDepositTransactionListResponse(
        String status,
        String message,
        List<MiddlewareFixedDepositTransactionResponse> data
) {}