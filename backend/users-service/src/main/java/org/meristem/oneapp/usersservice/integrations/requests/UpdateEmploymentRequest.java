package org.meristem.oneapp.usersservice.integrations.requests;

import lombok.Builder;

@Builder
public record UpdateEmploymentRequest(
        String employerName,
        String employmentJobTitle,
        String occupation
) {
}
