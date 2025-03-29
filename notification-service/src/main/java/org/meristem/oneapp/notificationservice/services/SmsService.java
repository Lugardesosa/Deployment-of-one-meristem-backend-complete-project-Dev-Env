package org.meristem.oneapp.notificationservice.services;

import org.meristem.oneapp.kafka.dtos.MessageDto;
import org.meristem.oneapp.notificationservice.services.interfaces.NotificationService;

public class SmsService implements NotificationService<MessageDto> {

    @Override
    public void send(MessageDto request) {

    }
}
