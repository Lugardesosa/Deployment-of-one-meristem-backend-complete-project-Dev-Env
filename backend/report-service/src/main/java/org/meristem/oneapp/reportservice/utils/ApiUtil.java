package org.meristem.oneapp.reportservice.utils;

import lombok.experimental.UtilityClass;
import org.meristem.oneapp.reportservice.domains.responses.AppResponse;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

@UtilityClass
public final class ApiUtil {

    public static <T> ResponseEntity<AppResponse<T>> buildResponse(T data, String status, String message) {
        return ResponseEntity.ok(AppResponse.<T>builder().data(data).status(status)
                .message(message).timestamp(LocalDateTime.now()).build());
    }
}
