package org.meristem.oneapp.coreservices.notifications.integrations.responses;

public record SmsNotificationResponse(
        String statusCode,

        String statusDescription,

        Double cost,

        String transactionRef,

        String transactionId
) {
}
