package org.meristem.oneapp.notificationservice.integrations.responses;


import java.util.List;

public record ExpoPushNotificationResponse(List<ExpoPushResponse> data) {

    public record ExpoPushResponse(String status, String id, String message, DetailsResponse details) implements ExpoData {

        public record DetailsResponse(String error, String expoPushToken) {
        }
    }

}