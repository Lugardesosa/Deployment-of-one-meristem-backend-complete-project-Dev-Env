package org.meristem.oneapp.walletservice.domains.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record WithdrawFundRequest(@NotBlank(message = "Cannot be blank") String toAccount, @NotNull(message = "Cannot be null")
                                  BigDecimal amount, @NotBlank(message = "Cannot be blank") String narration, @NotBlank(message = "Cannot be blank") String bankCode) {
}
