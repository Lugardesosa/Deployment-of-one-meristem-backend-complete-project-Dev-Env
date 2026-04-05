package org.meristem.oneapp.walletservice.integrations.requests;

import java.math.BigDecimal;

public record WithdrawalRequest(
        BigDecimal amount,
        String narration
) {
}
