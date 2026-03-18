package org.meristem.oneapp.notificationservice.services.implementations;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.meristem.oneapp.kafka.dtos.PushNotificationDto;
import org.meristem.oneapp.kafka.dtos.WebSocketDto;
import org.meristem.oneapp.notificationservice.constants.KafkaTopics;
import org.meristem.oneapp.notificationservice.domains.enums.PushNotificationMessages;
import org.meristem.oneapp.notificationservice.domains.requests.HollaTagsCallbackRequest;
import org.meristem.oneapp.notificationservice.domains.requests.TransferPaymentRequest;
import org.meristem.oneapp.notificationservice.domains.responses.HollaTagsCallbackResponse;
import org.meristem.oneapp.notificationservice.domains.responses.MiddlewareTransactionResponse;
import org.meristem.oneapp.notificationservice.domains.responses.TransactionTransferResponse;
import org.meristem.oneapp.notificationservice.services.ICallbackService;
import org.meristem.oneapp.notificationservice.services.IKafkaSenderService;
import org.meristem.oneapp.notificationservice.services.IPushNotificationService;
import org.meristem.oneapp.notificationservice.services.IWebsocketService;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class CallbackService implements ICallbackService {

    private final IWebsocketService websocketService;
    private final IPushNotificationService pushNotificationService;
    private final IKafkaSenderService kafkaSenderService;

    @Override
    public HollaTagsCallbackResponse handleHollaTags(HollaTagsCallbackRequest request) {
        return new HollaTagsCallbackResponse("ok");
    }

    @Override
    public MiddlewareTransactionResponse transactionCallback(TransferPaymentRequest request) {

        String paymentWebhook = "/topic/payment/";
        TransactionTransferResponse response = new TransactionTransferResponse();
        WebSocketDto responseWebSocketDto = new WebSocketDto(paymentWebhook + request.transRef(), response);
        PushNotificationDto pushNotificationDto = PushNotificationDto.builder().title(PushNotificationMessages.TRANSFER_SUCCESSFUL.getTitle())
                .body(PushNotificationMessages.TRANSFER_SUCCESSFUL.getBody().formatted("3000", "naira")).build();
        kafkaSenderService.send(responseWebSocketDto, Map.of(KafkaHeaders.TOPIC, KafkaTopics.KAFKA_WEB_SOCKET_TOPIC, KafkaHeaders.KEY, request.transRef()));
        kafkaSenderService.send(pushNotificationDto, Map.of(KafkaHeaders.TOPIC, KafkaTopics.KAFKA_PUSH_NOTIFICATION_TOPIC, KafkaHeaders.KEY, request.transRef()));

        return new MiddlewareTransactionResponse();
    }
}
