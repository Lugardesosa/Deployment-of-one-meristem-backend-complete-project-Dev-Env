package org.meristem.oneapp.notificationservice.services;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.meristem.oneapp.kafka.dtos.WebSocketDto;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class WebsocketService {

    private final SimpMessagingTemplate messagingTemplate;

    public void sendWebsocketMessage(WebSocketDto message) {
        messagingTemplate.convertAndSend(message.url(), message.body());
    }
}
