package org.meristem.oneapp.usersservice.domains.responses;

import lombok.Builder;

@Builder
public record OnboardingResponse(Long featureId, Boolean status) {
}
