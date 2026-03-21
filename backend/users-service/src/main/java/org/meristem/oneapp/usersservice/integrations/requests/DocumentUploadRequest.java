package org.meristem.oneapp.usersservice.integrations.requests;

import lombok.Builder;

@Builder
public record DocumentUploadRequest(
        String documentType,
        String documentNumber,
        String documentUrl,
        String remarks
) {
}
