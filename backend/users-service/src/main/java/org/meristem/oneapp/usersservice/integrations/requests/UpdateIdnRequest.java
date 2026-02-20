package org.meristem.oneapp.usersservice.integrations.requests;

import lombok.Builder;

@Builder
public record UpdateIdnRequest(
        String identityNumber,
        String identityType
) {
}
