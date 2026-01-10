package org.meristem.oneapp.notificationservice.services.implementations;

import lombok.extern.slf4j.Slf4j;
import org.meristem.oneapp.kafka.dtos.MessageDto;
import org.meristem.oneapp.notificationservice.services.NotificationService;
import org.springframework.stereotype.Service;

@Service("WHATSAPP")
@Slf4j
public class WhatsappService implements NotificationService<MessageDto> {

    @Override
    public void send(MessageDto request) {
        log.info("Sending Whatsapp message");
    }
}
