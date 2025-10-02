package org.meristem.oneapp.notificationservice.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringSubstitutor;
import org.meristem.oneapp.kafka.dtos.MessageDto;
import org.meristem.oneapp.notificationservice.config.configProperties.CreditSwitchProperties;
import org.meristem.oneapp.notificationservice.domains.enums.MessageType;
import org.meristem.oneapp.notificationservice.dtos.messaging.Message;
import org.meristem.oneapp.notificationservice.integrations.CreditSwitchClient;
import org.meristem.oneapp.notificationservice.integrations.requests.SmsNotificationRequest;
import org.meristem.oneapp.notificationservice.integrations.responses.SmsNotificationResponse;
import org.meristem.oneapp.notificationservice.mappers.MessageDtoToMessageMapper;
import org.meristem.oneapp.notificationservice.services.interfaces.NotificationService;
import org.meristem.oneapp.notificationservice.utils.AppUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;


@RequiredArgsConstructor
@Service("SMS")
@Slf4j
public class SmsService implements NotificationService<MessageDto> {

    private final CreditSwitchClient creditSwitchClient;
    private final MessageDtoToMessageMapper messageMapper = MessageDtoToMessageMapper.INSTANCE;
    private final CreditSwitchProperties creditSwitchProperties;
    private final ObjectMapper mapper;
    private final Map<String, String> textMessages;

    @Value("${spring.profiles.active}")
    private String activeProfile;


    @Override
    public void send(MessageDto messageDto) {
        if ("local".equals(activeProfile)) return;

        Message message = unbox(messageDto, mapper, messageMapper);

        try {
            String transactionRef = AppUtil.generatePassword(20);
            SmsNotificationRequest request = SmsNotificationRequest.builder()
                    .loginId(creditSwitchProperties.loginId())
                    .key(creditSwitchProperties.publicKey())
                    .senderId(creditSwitchProperties.senderId())
                    .msisdn(message.getRecipient().length == 1 ? message.getRecipient()[0] : message.getRecipient())
                    .messageBody(getBody(messageDto, message))
                    .checksum(getCreditSwitchSmsChecksum(transactionRef))
                    .transactionRef(transactionRef)
                    .build();

            SmsNotificationResponse response = creditSwitchClient.sendSms(request);
            log.info("SMS with transactionRef {} sent to {}", response.transactionRef(), maskNumber(message.getRecipient()));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private String getBody(MessageDto messageDto, Message message) {
        String body = message.getBody();
        switch (messageDto.type()) {
            case OTP -> {
                String template = textMessages.get(MessageType.OTP.getLabel());
                StringSubstitutor sub = new StringSubstitutor(message.getContext());
                body = sub.replace(template);
            }
            case LOGIN_SUCCESSFUL -> {}
            case NOTIFICATION -> {}
            case PASSWORD_RESET -> {}
        }
        return body;
    }

    private String getCreditSwitchSmsChecksum(String transactionRef) {

        String loginId = creditSwitchProperties.loginId();
        String privateKey = creditSwitchProperties.privateKey();

        String concatString = loginId + "|" + privateKey + "|" + transactionRef;
        int bcryptPasswordLen = 72;
        String salt = BCrypt.gensalt();
        byte[] hashedBytes = BCrypt.hashpw(concatString.substring(0, Math.min(bcryptPasswordLen, concatString.length())), salt).getBytes(StandardCharsets.UTF_8);
        return Base64.getEncoder().encodeToString(hashedBytes);
    }

    private String[] maskNumber(String[] numbers) {
        String[] maskedNumbers = new String[numbers.length];
        for (int i = 0; i < numbers.length; i++) {
            maskedNumbers[i] = numbers[i].substring(0, 3) + "****" + numbers[i].substring(numbers[i].length() - 4);
        }
        return maskedNumbers;
    }
}
