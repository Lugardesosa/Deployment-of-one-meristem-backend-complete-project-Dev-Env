package org.meristem.oneapp.notificationservice.integrations.responses;

import com.fasterxml.jackson.annotation.JsonAlias;

public record OneSignalPushNotificationResponse(String id, @JsonAlias("external_id") String externalId) {
}
