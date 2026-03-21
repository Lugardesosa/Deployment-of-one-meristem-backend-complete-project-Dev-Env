package org.meristem.oneapp.walletservice.integrations.responses;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;

@Schema(name = "WalletApiResponse", description = "Middleware wallet API response wrapper.")
public record WalletApiResponse<T>(
        @Schema(example = "200")
        String status,
        @Schema(example = "Request successful")
        String message,
        T data,
        @Schema(example = "2026-02-23T09:45:00Z")
        OffsetDateTime timestamp) {
}
