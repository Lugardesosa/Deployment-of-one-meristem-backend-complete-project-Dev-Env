package org.meristem.oneapp.usersservice.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Builder
public record SanctionDto(

        List<String> addresses,

        List<String> sanctions,

        List<String> aliases,

        List<String> birthDate,

        List<String> countries,

        String name,

        String photo,

        String gender,

        int confidenceScore,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime checkCreationDate,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime lastScreenedDate,
        Boolean deceased
) {
    public record Dataset(
            String name,
            String url
    ) {
    }
}
