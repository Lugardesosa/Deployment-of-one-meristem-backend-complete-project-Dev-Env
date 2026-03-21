package org.meristem.oneapp.usersservice.integrations.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record MiddlewareCustomerPositionResponse(
        @JsonProperty("customer_id") String customerId,
        @JsonProperty("total_balance") BigDecimal totalBalance,
        @JsonProperty("net_worth") BigDecimal netWorth,
        @JsonProperty("total_investments") BigDecimal totalInvestments,
        @JsonProperty("total_liabilities") BigDecimal totalLiabilities
) {
}
