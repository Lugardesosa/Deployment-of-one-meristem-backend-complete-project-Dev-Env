package org.meristem.oneapp.usersservice.integrations.responses;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;

public record MiddlewareRelationResponse(
        List<JsonNode> data
) {
}
