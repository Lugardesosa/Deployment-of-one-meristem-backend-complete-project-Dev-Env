package org.meristem.oneapp.wealthservice.integrations.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MiddlewareInvestmentProductResponse(
        @JsonProperty("product_id") String productId,
        @JsonProperty("product_display_description") String name,
        @JsonProperty("currency_id") String currency,
        @JsonProperty("fund_description") String category
){
}
