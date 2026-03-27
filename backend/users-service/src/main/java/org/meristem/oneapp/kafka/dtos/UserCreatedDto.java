package org.meristem.oneapp.kafka.dtos;

import lombok.Builder;

@Builder
public record UserCreatedDto(String middleWareCustomerId, Long parentUserId) {
}
