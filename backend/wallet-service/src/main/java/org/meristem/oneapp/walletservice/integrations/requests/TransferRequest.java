package org.meristem.oneapp.walletservice.integrations.requests;


import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record TransferRequest(String toAccountNo, BigDecimal amount, String narration) {
}
