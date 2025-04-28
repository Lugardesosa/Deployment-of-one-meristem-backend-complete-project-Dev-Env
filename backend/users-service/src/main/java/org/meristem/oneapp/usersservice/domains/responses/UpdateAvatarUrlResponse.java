package org.meristem.oneapp.usersservice.domains.responses;

import lombok.Builder;

@Builder
public record UpdateAvatarUrlResponse(Boolean status, String message) {
}
