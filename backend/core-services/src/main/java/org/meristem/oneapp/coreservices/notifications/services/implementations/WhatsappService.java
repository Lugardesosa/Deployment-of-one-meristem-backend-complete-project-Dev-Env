package org.meristem.oneapp.coreservices.notifications.services.implementations;

import lombok.extern.slf4j.Slf4j;
import org.meristem.oneapp.coreservices.notifications.services.INotificationService;
import org.meristem.oneapp.kafka.dtos.MessageDto;
import org.springframework.stereotype.Service;

@Service("WHATSAPP")
@Slf4j
public class WhatsappService implements INotificationService<MessageDto> {

    @Override
    public void send(MessageDto request) {
        log.info("Sending Whatsapp message");
    }
}
