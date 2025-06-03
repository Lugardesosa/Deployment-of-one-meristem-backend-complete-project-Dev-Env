package org.meristem.oneapp.walletservice.domains.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.io.Serializable;

@Builder
public record AppResponse<T>(@Schema(example = "2025-05-22T04:46:31.490689") String timestamp, @Schema(example = "200 OK") String status, @Schema(example = "Successful") String message, T data, Object metadata) implements Serializable {
}
