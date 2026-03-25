package org.meristem.oneapp.coreservices.notifications.services.implementations;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringSubstitutor;
import org.jspecify.annotations.NonNull;
import org.meristem.oneapp.coreservices.notifications.config.configProperties.CreditSwitchProperties;
import org.meristem.oneapp.coreservices.notifications.config.configProperties.HollaTagsProperties;
import org.meristem.oneapp.coreservices.notifications.domains.enums.MessageType;
import org.meristem.oneapp.coreservices.notifications.dtos.messaging.Message;
import org.meristem.oneapp.coreservices.notifications.integrations.CreditSwitchClient;
import org.meristem.oneapp.coreservices.notifications.integrations.HollaTagsClient;
import org.meristem.oneapp.coreservices.notifications.integrations.requests.SmsNotificationRequest;
import org.meristem.oneapp.coreservices.notifications.integrations.responses.SmsNotificationResponse;
import org.meristem.oneapp.coreservices.notifications.mappers.MessageDtoToMessageMapper;
import org.meristem.oneapp.coreservices.notifications.services.INotificationService;
import org.meristem.oneapp.coreservices.notifications.utils.AppUtil;
import org.meristem.oneapp.kafka.dtos.MessageDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.nio.charset.StandardCharsets;
import java.util.*;


@RequiredArgsConstructor
@Service("SMS")
@Slf4j
public class SmsService implements INotificationService<MessageDto> {

    private final CreditSwitchClient creditSwitchClient;
    private final HollaTagsClient hollaTagsClient;
    private final MessageDtoToMessageMapper messageMapper = MessageDtoToMessageMapper.INSTANCE;
    private final CreditSwitchProperties creditSwitchProperties;
    private final HollaTagsProperties hollaTagsProperties;
    private final ObjectMapper mapper;
    private final Map<String, String> textMessages;

    @Value("${spring.profiles.active}")
    private String activeProfile;


    public void sendCS(MessageDto messageDto) {
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


    @Override
    public void send(MessageDto messageDto) {
        if ("local".equals(activeProfile)) return;

        Message message = unbox(messageDto, mapper, messageMapper);

        try {
            String messageUuid = UUID.randomUUID().toString();

            MultiValueMap<String, Object> request = new LinkedMultiValueMap<>();
            request.add("user", hollaTagsProperties.user());
            request.add("pass", hollaTagsProperties.pass());
            request.add("callbackUrl", hollaTagsProperties.callbackUrl());
            request.add("msg", getBody(messageDto, message));
            request.add("to", message.getRecipient().length == 1 ? message.getRecipient()[0] : getHollaTagsToNumbers(message));
            request.add("from", hollaTagsProperties.from());
            request.add("type", 0);
            request.add("messageUuid", messageUuid);

            String response = hollaTagsClient.sendSms(request);
            log.info(((!Objects.equals(response, "sent")) ? "SMS with messageUuid {} not sent. Details: {}" : "SMS with messageUuid {} sent . Details: {}"), messageUuid, response);
        } catch (Exception e) {
            log.error("Unable to send sms", e);
        }
    }

    private static @NonNull String getHollaTagsToNumbers(Message message) {
        String[] numbers = message.getRecipient().length > 500 ? Arrays.copyOfRange(message.getRecipient(), 0, 500) : message.getRecipient();
        return String.join(",", numbers).replaceAll("\\+", "");
    }

    private String getBody(MessageDto messageDto, Message message) {
        String body = message.getBody();
        switch (messageDto.type()) {
            case OTP -> {
                String template = textMessages.get(MessageType.OTP.getLabel());
                StringSubstitutor sub = new StringSubstitutor(message.getContext());
                body = sub.replace(template);
            }
            case LOGIN_SUCCESSFUL -> {
            }
            case NOTIFICATION -> {
            }
            case PASSWORD_RESET -> {
            }
            case EMAIL_CONFIRMATION -> {

            }
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
