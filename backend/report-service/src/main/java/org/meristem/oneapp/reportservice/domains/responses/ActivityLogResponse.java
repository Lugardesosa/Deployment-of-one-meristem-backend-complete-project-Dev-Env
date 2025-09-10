package org.meristem.oneapp.reportservice.domains.responses;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Map;

@Builder

@Schema(example = """
                    {
                        "id": 445,
                        "activityDate": "2025-09-10 12:40:16",
                        "actor": "Lenora62@yahoo.com",
                        "action": "CREATED",
                        "entity": "Cash",
                        "entityId": "4",
                        "application": "trusties-service",
                        "activity": "Asset Category ‘Cash’ Created - Ramiro Luettgen",
                        "metadata": {}
                    }
        """, description = "Activity logs for each action")
public record ActivityLogResponse(Long id, LocalDateTime activityDate, String actor, String action, String entity, String entityId,
                                  String application, String activity, Map<String, Object> metadata, String description) {
}
