package org.meristem.oneapp.notificationservice.domains.responses;

import lombok.Builder;

@Builder
public record MiddlewareTransactionResponse(Boolean success, String message) {
}
