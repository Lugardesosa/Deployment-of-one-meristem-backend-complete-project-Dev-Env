package org.meristem.oneapp.coreservices.notifications.domains.responses;

import lombok.Builder;

@Builder
public record MiddlewareTransactionResponse(Boolean success, String message) {
}
