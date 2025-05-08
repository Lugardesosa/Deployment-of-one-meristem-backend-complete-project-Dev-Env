package org.meristem.oneapp.notificationservice.services.interfaces;

import org.meristem.oneapp.kafka.dtos.MessageDto;

public class WhatsappService implements NotificationService<MessageDto> {
    @Override
    public void send(MessageDto request) {

    }
}
