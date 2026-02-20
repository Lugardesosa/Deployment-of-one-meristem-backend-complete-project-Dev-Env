package org.meristem.oneapp.kafka.dtos;

import lombok.Builder;

@Builder
public record UploadImageDto(String photo, Long userId, String userEmail, Long userIdDetailsId) {
}
