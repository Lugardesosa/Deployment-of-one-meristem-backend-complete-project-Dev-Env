package org.meristem.oneapp.reportservice.domains.responses;


import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Map;

@Builder
public record ActivityLogResponse(Long id, LocalDateTime activityDate, String actor, String action, String entity, String entityId,
                                  String application, Map<String, Object> metadata, String description) {
}
