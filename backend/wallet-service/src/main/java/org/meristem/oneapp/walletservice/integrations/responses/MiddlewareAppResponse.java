package org.meristem.oneapp.walletservice.domains.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Builder
public record AppResponse<T>(ZonedDateTime timestamp, String status, String message, T data, Object metadata) implements Serializable {
}
