package org.meristem.oneapp.walletservice.integrations.requests;

import lombok.Builder;

@Builder
public record VerifyPinRequest(Long userId, String pin) {
}
