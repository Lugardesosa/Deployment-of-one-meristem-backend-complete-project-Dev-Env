package org.meristem.oneapp.notificationservice.integrations.requests;

import lombok.Builder;

import java.util.List;
import java.util.Map;

@Builder public record ExpoPushNotificationRequest(
        List<String> to,
        String sound,
        String badge,
        String title,
        String body,
        Map<String, Object> data
) {


}
