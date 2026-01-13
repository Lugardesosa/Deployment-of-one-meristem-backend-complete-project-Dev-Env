package org.meristem.oneapp.notificationservice.services.implementations;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.meristem.oneapp.kafka.dtos.WebSocketDto;
import org.meristem.oneapp.notificationservice.services.IWebsocketService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class WebsocketService implements IWebsocketService {

    private final SimpMessagingTemplate messagingTemplate;

    public void sendWebsocketMessage(WebSocketDto message) {
        messagingTemplate.convertAndSend(message.url(), message.body());
    }
}
