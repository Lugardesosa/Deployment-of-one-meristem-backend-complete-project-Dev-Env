package org.meristem.oneapp.notificationservice.integrations.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;

@Builder
public record OneSignalPushNotificationRequest(
        @JsonProperty("app_id")
        String appId,

        Contents contents,

        @JsonProperty("target_channel")
        String targetChannel,

        @JsonProperty("include_subscription_ids")
        List<String> subscriptionIds
) {

    public record Contents(String en) {}
}
