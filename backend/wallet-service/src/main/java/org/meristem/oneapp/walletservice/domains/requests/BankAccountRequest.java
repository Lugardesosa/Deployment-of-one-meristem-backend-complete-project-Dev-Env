package org.meristem.oneapp.walletservice.domains.requests;

import jakarta.validation.constraints.NotBlank;

public record BankAccountRequest(@NotBlank(message = "Cannot be blank") String accountNumber, @NotBlank(message = "Cannot be blank") String bankCode) {
}
