package org.meristem.oneapp.usersservice.integrations.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MiddlewareDocumentStatusResponse(
        String status,
        @JsonProperty("upload_date") String uploadDate,
        @JsonProperty("approval_status") String approvalStatus
) {
}
