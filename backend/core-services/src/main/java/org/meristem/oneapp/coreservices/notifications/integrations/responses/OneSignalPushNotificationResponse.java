package org.meristem.oneapp.coreservices.notifications.integrations.responses;

import com.fasterxml.jackson.annotation.JsonAlias;

public record OneSignalPushNotificationResponse(String id, @JsonAlias("external_id") String externalId) {
}
