package org.meristem.oneapp.usersservice.integrations.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MiddlewareDocumentBase64Response(
        @JsonProperty("customer_id") String customerId,
        @JsonProperty("document_mime_type") String documentMimeType,
        @JsonProperty("document_content_base64") String documentContentBase64
) {
}
