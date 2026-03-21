package org.meristem.oneapp.kafka.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmailConfirmationDto {
    private String[] recipient;
    private String subject;
    private String code;
    private String firstName;
    private String link;
}

