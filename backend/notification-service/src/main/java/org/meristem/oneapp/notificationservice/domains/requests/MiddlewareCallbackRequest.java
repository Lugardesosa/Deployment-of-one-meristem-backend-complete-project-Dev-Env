package org.meristem.oneapp.notificationservice.domains.requests;

import com.fasterxml.jackson.databind.JsonNode;
import org.meristem.oneapp.notificationservice.domains.enums.EventType;

public record MiddlewareCallbackRequest(EventType eventType, Long eventId, JsonNode data) {
//public record TransferPaymentRequest(String transRef, BigDecimal amount, String customerId, String currency) {
}
