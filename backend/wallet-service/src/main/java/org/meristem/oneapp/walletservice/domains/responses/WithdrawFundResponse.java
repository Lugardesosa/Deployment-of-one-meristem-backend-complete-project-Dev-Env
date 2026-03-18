package org.meristem.oneapp.walletservice.domains.responses;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record WithdrawFundResponse(String status, String transferReference, BigDecimal amount) {
}
