package org.meristem.oneapp.usersservice.domains.responses;

import lombok.Builder;

@Builder
public record WebhookResponse(boolean success, String message) {
}
