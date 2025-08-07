package org.meristem.oneapp.coreservice.integrations.responses;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;


//@AllArgsConstructor
//@NoArgsConstructor
@Builder
//@Data
public record AppBaseResponse<T>(@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
                                 LocalDateTime timestamp,
                                 String status,
                                 String message,
                                 T data,
                                 Object metadata) implements Serializable {

//    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
//    private LocalDateTime timestamp;
//    private String status;
//    private String message;
//    private T data;
//    private Object metadata;
}
