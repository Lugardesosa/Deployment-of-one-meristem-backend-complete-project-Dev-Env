package org.meristem.oneapp.coreservices.notifications.integrations.responses;

import java.util.Map;

public record ExpoPushReceiptResponse(Map<String, ExpoPushResponse> data) {

    public record ExpoPushResponse(String status, String id, String message, DetailsResponse details) implements ExpoData {

        public record DetailsResponse(String error, String expoPushToken) {
        }
    }
}
