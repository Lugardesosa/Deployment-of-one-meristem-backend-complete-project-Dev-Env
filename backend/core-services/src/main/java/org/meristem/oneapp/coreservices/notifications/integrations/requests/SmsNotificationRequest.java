package org.meristem.oneapp.coreservices.notifications.integrations.requests;

import lombok.Builder;

@Builder
public record SmsNotificationRequest(
        String loginId,

        String key,

        String senderId,

        Object msisdn,

        String messageBody,

        String transactionRef,

        String checksum
) {
}
