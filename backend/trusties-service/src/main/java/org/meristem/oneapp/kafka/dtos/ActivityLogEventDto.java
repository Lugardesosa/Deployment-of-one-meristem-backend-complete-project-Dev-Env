package org.meristem.oneapp.kafka.dtos;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Map;

@Builder
public record ActivityLogEventDto(LocalDateTime activityDate, String actor, String action, String entity, Long entityId,
                                  String application, Map<String, Object> metadata) {
}
