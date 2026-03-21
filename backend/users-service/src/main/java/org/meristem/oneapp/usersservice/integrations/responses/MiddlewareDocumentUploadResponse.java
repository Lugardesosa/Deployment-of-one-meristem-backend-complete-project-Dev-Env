package org.meristem.oneapp.usersservice.integrations.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MiddlewareDocumentUploadResponse(
        @JsonProperty("document_id") String documentId
) {
}
