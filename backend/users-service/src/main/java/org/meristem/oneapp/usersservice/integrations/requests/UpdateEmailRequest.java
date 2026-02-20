package org.meristem.oneapp.usersservice.integrations.requests;

import lombok.Builder;

@Builder
public record UpdateEmailRequest(
        String emailAddress,
        Boolean isPrimary
) {
}
