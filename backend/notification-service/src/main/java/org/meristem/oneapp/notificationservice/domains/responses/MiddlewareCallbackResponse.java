package org.meristem.oneapp.notificationservice.domains.responses;

import lombok.Builder;

@Builder
public record MiddlewareCallbackResponse(Boolean success, String message) {
}
