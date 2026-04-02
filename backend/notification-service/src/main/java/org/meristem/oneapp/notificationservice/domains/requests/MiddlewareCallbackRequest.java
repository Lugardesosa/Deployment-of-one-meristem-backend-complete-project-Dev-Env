package org.meristem.oneapp.notificationservice.domains.requests;

import com.fasterxml.jackson.databind.JsonNode;
import org.meristem.oneapp.notificationservice.domains.enums.EventType;

import java.util.Map;

public record MiddlewareCallbackRequest(EventType eventType, Long eventId, Map<String, Object> data) {
}
