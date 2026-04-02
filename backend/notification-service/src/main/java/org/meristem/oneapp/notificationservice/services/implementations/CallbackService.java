package org.meristem.oneapp.notificationservice.services.implementations;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.meristem.oneapp.kafka.dtos.PushNotificationDto;
import org.meristem.oneapp.kafka.dtos.WebSocketDto;
import org.meristem.oneapp.notificationservice.constants.KafkaTopics;
import org.meristem.oneapp.notificationservice.domains.enums.PushNotificationMessages;
import org.meristem.oneapp.notificationservice.domains.requests.HollaTagsCallbackRequest;
import org.meristem.oneapp.notificationservice.domains.requests.MiddlewareCallbackRequest;
import org.meristem.oneapp.notificationservice.domains.requests.PayoutRequest;
import org.meristem.oneapp.notificationservice.domains.requests.TransferPaymentRequest;
import org.meristem.oneapp.notificationservice.domains.responses.HollaTagsCallbackResponse;
import org.meristem.oneapp.notificationservice.domains.responses.MiddlewareCallbackResponse;
import org.meristem.oneapp.notificationservice.domains.responses.TransactionTransferResponse;
import org.meristem.oneapp.notificationservice.integrations.UserServiceClient;
import org.meristem.oneapp.notificationservice.services.ICallbackService;
import org.meristem.oneapp.notificationservice.services.IKafkaSenderService;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class CallbackService implements ICallbackService {

    private final IKafkaSenderService kafkaSenderService;
    private final UserServiceClient userServiceClient;
    private final ObjectMapper objectMapper;

    @Override
    public HollaTagsCallbackResponse handleHollaTags(HollaTagsCallbackRequest request) {
        return new HollaTagsCallbackResponse("ok");
    }

    public MiddlewareCallbackResponse transactionCallback(TransferPaymentRequest request) {

        String paymentWebhook = "/topic/payment/" + request.customerId();
        Long userId = userServiceClient.getUserId(request.customerId()).data();
        TransactionTransferResponse response = new TransactionTransferResponse(request.amount(), request.customerId(), request.currency());
        WebSocketDto responseWebSocketDto = new WebSocketDto(paymentWebhook + request.transRef(), response);
        PushNotificationDto pushNotificationDto = PushNotificationDto.builder().title(PushNotificationMessages.TRANSFER_SUCCESSFUL.getTitle()).customerId(request.customerId())
                .body(PushNotificationMessages.TRANSFER_SUCCESSFUL.getBody().formatted(request.amount(), request.currency())).userId(userId).build();
        kafkaSenderService.send(responseWebSocketDto, Map.of(KafkaHeaders.TOPIC, KafkaTopics.KAFKA_WEB_SOCKET_TOPIC, KafkaHeaders.KEY, request.transRef()));
        kafkaSenderService.send(pushNotificationDto, Map.of(KafkaHeaders.TOPIC, KafkaTopics.KAFKA_PUSH_NOTIFICATION_TOPIC, KafkaHeaders.KEY, request.transRef()));

        return new MiddlewareCallbackResponse(true, "success");
    }

    public MiddlewareCallbackResponse payoutCallback(PayoutRequest request) {

//        String paymentWebhook = "/topic/payment/" + request.customerId();
//        Long userId = userServiceClient.getUserId(request.customerId()).data();
//        TransactionTransferResponse response = new TransactionTransferResponse(request.amount(), request.customerId(), request.currency());
//        WebSocketDto responseWebSocketDto = new WebSocketDto(paymentWebhook + request.transRef(), response);
//        PushNotificationDto pushNotificationDto = PushNotificationDto.builder().title(PushNotificationMessages.TRANSFER_SUCCESSFUL.getTitle()).customerId(request.customerId())
//                .body(PushNotificationMessages.TRANSFER_SUCCESSFUL.getBody().formatted(request.amount(), request.currency())).userId(userId).build();
//        kafkaSenderService.send(responseWebSocketDto, Map.of(KafkaHeaders.TOPIC, KafkaTopics.KAFKA_WEB_SOCKET_TOPIC, KafkaHeaders.KEY, request.transRef()));
//        kafkaSenderService.send(pushNotificationDto, Map.of(KafkaHeaders.TOPIC, KafkaTopics.KAFKA_PUSH_NOTIFICATION_TOPIC, KafkaHeaders.KEY, request.transRef()));

        return new MiddlewareCallbackResponse(true, "success");
    }

    @Override
    public MiddlewareCallbackResponse middlewareCallback(MiddlewareCallbackRequest request) {

        return switch (request.eventType()) {
            case TRANSACTION -> transactionCallback(objectMapper.convertValue(request.data(), TransferPaymentRequest.class));
            case PAYOUT -> payoutCallback(objectMapper.convertValue(request.data(), PayoutRequest.class));
        };
    }
}
