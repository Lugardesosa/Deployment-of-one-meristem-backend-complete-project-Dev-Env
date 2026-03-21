package org.meristem.oneapp.walletservice.integrations.responses;

import lombok.Builder;

@Builder
public record UpdateResponse(String message, Boolean success) {
}
