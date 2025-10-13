package org.meristem.oneapp.usersservice.domains.responses;

import lombok.Builder;

@Builder
public record OkHiWebhookResponse(boolean success, String message) {
}
