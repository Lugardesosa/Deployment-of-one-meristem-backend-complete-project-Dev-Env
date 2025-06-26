package org.meristem.oneapp.walletservice.domains.responses;

import lombok.Builder;

@Builder
public record ProvidusTransactionResponse(String message, Boolean success) {
}
