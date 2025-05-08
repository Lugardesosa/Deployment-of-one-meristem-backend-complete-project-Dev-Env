package org.meristem.oneapp.notificationservice.services;

import lombok.extern.slf4j.Slf4j;
import org.meristem.oneapp.kafka.dtos.MessageDto;
import org.meristem.oneapp.notificationservice.services.interfaces.NotificationService;
import org.springframework.stereotype.Service;

@Service("SMS")
@Slf4j
public class SmsService implements NotificationService<MessageDto> {

    @Override
    public void send(MessageDto request) {
        log.info("Sending SMS");
    }
}
