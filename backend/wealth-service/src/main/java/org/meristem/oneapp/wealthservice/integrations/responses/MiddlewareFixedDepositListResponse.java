package org.meristem.oneapp.wealthservice.integrations.responses;

import java.util.List;

public record MiddlewareFixedDepositListResponse(
        String status,
        String message,
        List<MiddlewareFixedDepositResponse> data
) {}