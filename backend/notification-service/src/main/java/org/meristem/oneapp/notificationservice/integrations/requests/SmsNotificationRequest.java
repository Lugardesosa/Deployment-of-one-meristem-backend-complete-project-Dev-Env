package org.meristem.oneapp.notificationservice.integrations.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
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
