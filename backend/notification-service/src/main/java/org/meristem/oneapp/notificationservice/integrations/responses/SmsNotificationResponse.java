package org.meristem.oneapp.notificationservice.integrations.responses;

public record SmsNotificationResponse(
        String statusCode,

        String statusDescription,

        Double cost,

        String transactionRef,

        String transactionId
) {
}
