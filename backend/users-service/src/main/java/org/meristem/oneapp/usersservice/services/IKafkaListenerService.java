package org.meristem.oneapp.usersservice.services;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.meristem.oneapp.kafka.dtos.*;
import org.meristem.oneapp.usersservice.constants.KafkaTopics;
import org.springframework.kafka.annotation.KafkaListener;

/**
 * Interface for handling Kafka message consumption.
 * Provides functionality for listening to Kafka topics and processing messages.
 */
public interface IKafkaListenerService {
    // Currently no methods - placeholder for future Kafka listener methods

    void listenKycCompleted(ConsumerRecord<String, KycCompletedDto> record);

    void createCustomer(ConsumerRecord<String, CreateCustomerDto> record);

    @KafkaListener(topicPattern = KafkaTopics.KAFKA_DEPENDENT_CREATE_TOPIC)
    void createDependent(ConsumerRecord<String, CreateCustomerDto> record);

    void createJointCustomer(ConsumerRecord<String, CreateJointCustomerDto> record);

    void addressVerified(ConsumerRecord<String, CustomerAddressVerifiedDto> record);

    void uploadImage(ConsumerRecord<String, UploadImageDto> record);
}
