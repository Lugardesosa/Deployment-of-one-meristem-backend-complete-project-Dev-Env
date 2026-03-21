package org.meristem.oneapp.usersservice.services;

import java.util.Map;

/**
 * Interface for sending messages to Kafka topics.
 * Provides multiple methods to send messages with different configurations.
 *
 * @author Kingsley
 */
public interface IKafkaSenderService {

    /**
     * Sends a message to the specified Kafka topic.
     *
     * @param topic   the name of the Kafka topic
     * @param payload the message payload to send
     */
    void send(String topic, Object payload);

    /**
     * Sends a message with custom headers to a Kafka topic.
     *
     * @param payload the message payload to send
     * @param headers a map of custom headers to include in the message
     */
    void send(Object payload, Map<String, Object> headers);

    /**
     * Sends a message to a Kafka topic with a specified key.
     *
     * @param topic   the name of the Kafka topic
     * @param key     the key to associate with the message
     * @param payload the message payload to send
     */
    void send(String topic, String key, Object payload);

    /**
     * Sends a message to a specific partition of a Kafka topic with a specified key.
     *
     * @param topic     the name of the Kafka topic
     * @param partition the partition number to send the message to
     * @param key       the key to associate with the message
     * @param payload   the message payload to send
     */
    void send(String topic, Integer partition, String key, Object payload);

    /**
     * Sends a message to a specific partition of a Kafka topic with a specified key and timestamp.
     *
     * @param topic     the name of the Kafka topic
     * @param partition the partition number to send the message to
     * @param timeStamp the timestamp to associate with the message
     * @param key       the key to associate with the message
     * @param payload   the message payload to send
     */
    void send(String topic, Integer partition, Long timeStamp, String key, Object payload);
}
