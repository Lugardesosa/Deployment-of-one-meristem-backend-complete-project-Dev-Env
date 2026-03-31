package org.meristem.oneapp.wealthservice.integrations.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record MiddlewareInvestmentProductDataResponse(
        @JsonProperty("Data") List<MiddlewareInvestmentProductResponse> data
) {}