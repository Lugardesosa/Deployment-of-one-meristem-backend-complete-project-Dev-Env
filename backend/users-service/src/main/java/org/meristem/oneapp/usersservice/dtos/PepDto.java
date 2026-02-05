package org.meristem.oneapp.usersservice.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record PepDto(
        List<String> addresses,
        List<String> aliases,

        List<String> birthDate,

        List<String> countries,

        String name,

        String photo,

        String gender,

        Integer confidenceScore,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime checkCreationDate,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime lastScreenedDate,
        Boolean deceased
) {
}
