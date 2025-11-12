package org.meristem.oneapp.kafka.dtos;

import lombok.Builder;

import java.util.Map;

@Builder
public record PushNotificationDto(Long userId, String title, String body, Map<String, Object> data, boolean toAll) {
}
