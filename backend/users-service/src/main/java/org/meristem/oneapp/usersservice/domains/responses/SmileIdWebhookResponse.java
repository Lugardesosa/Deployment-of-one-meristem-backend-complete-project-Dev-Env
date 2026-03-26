package org.meristem.oneapp.usersservice.domains.responses;

public record SmileIdWebhookResponse(String message, Boolean status, BvnQueryResponse response) {
}
