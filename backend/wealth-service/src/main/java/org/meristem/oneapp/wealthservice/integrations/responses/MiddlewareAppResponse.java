package org.meristem.oneapp.wealthservice.integrations.responses;

import lombok.Builder;

import java.io.Serializable;
import java.time.ZonedDateTime;

@Builder
public record MiddlewareAppResponse<T>(ZonedDateTime timestamp, String status, String message, T data, Object metadata) implements Serializable {
}
