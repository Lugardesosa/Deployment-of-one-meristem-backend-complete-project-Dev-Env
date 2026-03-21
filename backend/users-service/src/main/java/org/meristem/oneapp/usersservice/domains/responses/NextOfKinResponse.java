package org.meristem.oneapp.usersservice.domains.responses;

import lombok.Builder;

@Builder
public record NextOfKinResponse(String fullName, String email, String phoneNumber, String relationship) {
}
