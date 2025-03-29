package org.meristem.oneapp.usersservice.domains.responses;


import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record AppResponse<T> (String timestamp, String status, String message, T data, Object metadata) {
}
