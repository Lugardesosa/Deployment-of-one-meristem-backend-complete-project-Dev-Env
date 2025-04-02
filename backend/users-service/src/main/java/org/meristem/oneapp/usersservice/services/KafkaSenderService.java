package org.meristem.oneapp.usersservice.services;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class KafkaSenderService<T> {


    private final KafkaTemplate<String, T> kafkaTemplate;

    public void send(String topic, T payload) {
        kafkaTemplate.send(topic, payload);
    }

    public void send(T payload, Map<String, Object> headers) {
        Message<T> message = MessageBuilder.withPayload(payload)
                .setHeaders(MessageHeaderAccessor.fromMap(headers)).build();
        kafkaTemplate.send(message);
    }

    public void send(String topic, String key, T payload) {
        kafkaTemplate.send (topic, key, payload);
    }

    public void send(String topic, Integer partition, String key, T payload) {
        kafkaTemplate.send(topic, partition, key, payload);
    }

    public void send(String topic, Integer partition, Long timeStamp, String key, T payload) {
        kafkaTemplate.send(topic, partition, timeStamp, key, payload);
    }
}
