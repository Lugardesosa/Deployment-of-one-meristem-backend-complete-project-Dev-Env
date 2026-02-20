package org.meristem.oneapp.usersservice.integrations.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;

public record MiddlewareCustomerPositionByModuleResponse(
        @JsonProperty("customer_id") String customerId,
        List<JsonNode> modules
) {
}
