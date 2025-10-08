package org.meristem.oneapp.kafka.dtos;

import java.time.LocalDateTime;
import java.util.Map;

public record ActivityLogEventDto(LocalDateTime activityDate, String actor, String action, String entity, Long entityId,
                                  String application, String activity, Map<String, Object> metadata) {
}
