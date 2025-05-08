package org.meristem.oneapp.usersservice.services;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Service;

import java.util.Map;


/**
 * Service class for sending messages to Kafka topics.
 * Provides multiple methods to send messages with different configurations, such as specifying headers, keys, partitions, and timestamps.
 * This service is designed to simplify Kafka message publishing by abstracting the underlying KafkaTemplate operations.
 *
 * @param <T> the type of the message payload
 *
 * @author Kingsley
 */
@Service
@RequiredArgsConstructor
public class KafkaSenderService<T> {


    private final KafkaTemplate<String, T> kafkaTemplate;

    /**
     * Sends a message to the specified Kafka topic.
     *
     * @param topic   the name of the Kafka topic
     * @param payload the message payload to send
     */
    public void send(String topic, T payload) {
        kafkaTemplate.send(topic, payload);
    }

    /**
     * Sends a message with custom headers to a Kafka topic.
     *
     * @param payload the message payload to send
     * @param headers a map of custom headers to include in the message
     */
    public void send(T payload, Map<String, Object> headers) {
        Message<T> message = MessageBuilder.withPayload(payload)
                .setHeaders(MessageHeaderAccessor.fromMap(headers)).build();
        kafkaTemplate.send(message);
    }

    /**
     * Sends a message to a Kafka topic with a specified key.
     *
     * @param topic   the name of the Kafka topic
     * @param key     the key to associate with the message
     * @param payload the message payload to send
     */
    public void send(String topic, String key, T payload) {
        kafkaTemplate.send (topic, key, payload);
    }

    /**
     * Sends a message to a specific partition of a Kafka topic with a specified key.
     *
     * @param topic     the name of the Kafka topic
     * @param partition the partition number to send the message to
     * @param key       the key to associate with the message
     * @param payload   the message payload to send
     */
    public void send(String topic, Integer partition, String key, T payload) {
        kafkaTemplate.send(topic, partition, key, payload);
    }

    /**
     * Sends a message to a specific partition of a Kafka topic with a specified key and timestamp.
     *
     * @param topic     the name of the Kafka topic
     * @param partition the partition number to send the message to
     * @param timeStamp the timestamp to associate with the message
     * @param key       the key to associate with the message
     * @param payload   the message payload to send
     */
    public void send(String topic, Integer partition, Long timeStamp, String key, T payload) {
        kafkaTemplate.send(topic, partition, timeStamp, key, payload);
    }
}
