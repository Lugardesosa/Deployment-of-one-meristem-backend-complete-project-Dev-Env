package org.meristem.oneapp.walletservice.domains.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record TransactionRequest(@NotNull(message = "Cannot be null") LocalDate startDate, @NotNull(message = "Cannot be null") LocalDate endDate) {
}
