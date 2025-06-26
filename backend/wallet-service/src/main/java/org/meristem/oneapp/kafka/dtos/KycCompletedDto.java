package org.meristem.oneapp.kafka.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public record KycCompletedDto(Long userId, String firstName, String lastName, String phoneNumber, String email, String address, String bvn, String dob) {
}
