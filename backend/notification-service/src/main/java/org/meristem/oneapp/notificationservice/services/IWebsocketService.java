package org.meristem.oneapp.notificationservice.services;

import org.meristem.oneapp.kafka.dtos.WebSocketDto;

public interface IWebsocketService {
    void sendWebsocketMessage(WebSocketDto message);
}
