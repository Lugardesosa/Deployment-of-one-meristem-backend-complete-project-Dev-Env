package org.meristem.oneapp.usersservice.domains.responses;


import lombok.Builder;

@Builder
public record SubmitOnboardingResponse(Boolean status, String message, Long featureId, String documentUrl) {
}
