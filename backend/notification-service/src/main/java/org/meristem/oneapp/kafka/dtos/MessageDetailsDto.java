package org.meristem.oneapp.kafka.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record MessageDetailsDto(String[] recipient, String subject, String body) {
}
