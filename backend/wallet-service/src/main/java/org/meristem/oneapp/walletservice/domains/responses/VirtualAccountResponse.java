package org.meristem.oneapp.walletservice.domains.responses;

import lombok.Builder;

@Builder
public record VirtualAccountResponse(String accountNumber, String bankName) {
}
