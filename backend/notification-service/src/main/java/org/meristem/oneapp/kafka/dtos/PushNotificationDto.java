package org.meristem.oneapp.kafka.dtos;

import java.util.Map;

public record PushNotificationDto(Long userId, String title, String body, Map<String, Object> data, boolean toAll) {
}
