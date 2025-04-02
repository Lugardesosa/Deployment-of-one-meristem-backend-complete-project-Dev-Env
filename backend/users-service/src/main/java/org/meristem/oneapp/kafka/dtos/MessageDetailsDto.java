package org.meristem.oneapp.kafka.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public record MessageDetailsDto(String[] recipient, String subject, String body) {
}
