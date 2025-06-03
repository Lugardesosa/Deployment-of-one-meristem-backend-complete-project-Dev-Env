package org.meristem.oneapp.notificationservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.meristem.oneapp.kafka.dtos.MessageDto;
import org.meristem.oneapp.notificationservice.config.configProperties.CreditSwitchProperties;
import org.meristem.oneapp.notificationservice.integrations.CreditSwitchClient;
import org.meristem.oneapp.notificationservice.integrations.requests.SmsNotificationRequest;
import org.meristem.oneapp.notificationservice.integrations.responses.SmsNotificationResponse;
import org.meristem.oneapp.notificationservice.services.interfaces.NotificationService;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;

@RequiredArgsConstructor
@Service("SMS")
@Slf4j
public class SmsService implements NotificationService<MessageDto> {

    private final CreditSwitchClient creditSwitchClient;
    private final CreditSwitchProperties creditSwitchProperties;

    @Override
    public void send(MessageDto messageDto) {

        String transactionRef = UUID.randomUUID().toString();
        SmsNotificationRequest request = SmsNotificationRequest.builder()
                .loginId(creditSwitchProperties.loginId())
                .key(creditSwitchProperties.publicKey())
                .senderId(creditSwitchProperties.senderId())
                .msisdn(messageDto.message().recipient().length == 1 ? messageDto.message().recipient()[0] : messageDto.message().recipient())
                .messageBody(messageDto.message().body())
                .transactionRef(transactionRef)
                .checksum(getCreditSwitchSmsChecksum(transactionRef)).build();

        SmsNotificationResponse response = creditSwitchClient.sendSms(request);
    }

    private String getCreditSwitchSmsChecksum(String transactionRef) {

        String loginId = creditSwitchProperties.loginId();
        String privateKey = creditSwitchProperties.privateKey();

        String concatString = loginId + "|" + privateKey + "|" + transactionRef;
        byte[] message = BCrypt.hashpw(concatString,BCrypt.gensalt()).getBytes(StandardCharsets.UTF_8);
        return Base64.getEncoder().encodeToString(message);
    }
}
