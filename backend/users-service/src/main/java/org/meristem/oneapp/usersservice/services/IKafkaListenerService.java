package org.meristem.oneapp.usersservice.services;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.meristem.oneapp.kafka.dtos.KycCompletedDto;

/**
 * Interface for handling Kafka message consumption.
 * Provides functionality for listening to Kafka topics and processing messages.
 */
public interface IKafkaListenerService {
    // Currently no methods - placeholder for future Kafka listener methods

    void listenKycCompleted(ConsumerRecord<String, KycCompletedDto> record);
}
