package org.meristem.oneapp.notificationservice.services;

import lombok.RequiredArgsConstructor;
import org.meristem.oneapp.kafka.dtos.MessageDto;
import org.meristem.oneapp.notificationservice.services.interfaces.NotificationService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service("EMAIL")
@RequiredArgsConstructor
public class EmailService implements NotificationService<MessageDto> {

    private final JavaMailSender mailSender;

    @Override
    public void send(MessageDto request) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("MS_ksWCqv@trial-p7kx4xw1oemg9yjr.mlsender.net");
        message.setTo(request.message().recipient());
        message.setSubject(request.message().subject());
        message.setText(request.message().body());
        mailSender.send(message);
    }
}
