package org.meristem.oneapp.coreservices.notifications.services;

import org.meristem.oneapp.kafka.dtos.WebSocketDto;

public interface IWebsocketService {
    void sendWebsocketMessage(WebSocketDto message);
}
