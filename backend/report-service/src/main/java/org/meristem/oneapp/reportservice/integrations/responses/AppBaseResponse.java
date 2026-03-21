package org.meristem.oneapp.reportservice.integrations.responses;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.io.Serializable;
import java.time.LocalDateTime;


@Builder
public record AppBaseResponse<T>(@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
                                 LocalDateTime timestamp,
                                 String status,
                                 String message,
                                 T data,
                                 Object metadata) implements Serializable {
}
