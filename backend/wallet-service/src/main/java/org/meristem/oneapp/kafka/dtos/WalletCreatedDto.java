package org.meristem.oneapp.kafka.dtos;

import lombok.Builder;

@Builder
public record WalletCreatedDto(String walletId, String customerId) {
}
