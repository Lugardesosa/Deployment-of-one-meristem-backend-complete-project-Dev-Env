package org.meristem.oneapp.usersservice.services.implementations;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.meristem.oneapp.kafka.dtos.KycCompletedDto;
import org.meristem.oneapp.usersservice.constants.KafkaTopics;
import org.meristem.oneapp.kafka.dtos.CreateCustomerDto;
import org.meristem.oneapp.kafka.dtos.UploadImageDto;
import org.meristem.oneapp.usersservice.services.IAmlService;
import org.meristem.oneapp.usersservice.services.IKafkaListenerService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
public class KafkaListenerService implements IKafkaListenerService {

    private final IAmlService amlService;
    private final UsersService usersService;
    private final IdDetailsService idDetailsService;

    @KafkaListener(topicPattern = KafkaTopics.KAFKA_KYC_COMPLETED)
    @Transactional
    @Override
    public void listenKycCompleted(ConsumerRecord<String, KycCompletedDto> record) {
        log.info("Received KycCompleted event: {}", record.value());
        amlService.performAmlRequest(record.value());
    }

    @KafkaListener(topicPattern = KafkaTopics.KAFKA_KYC_CUSTOMER_CREATE_TOPIC)
    @Override
    public void createCustomer(ConsumerRecord<String, CreateCustomerDto> record) {
        log.info("Received CreateCustomerDto event: {}", record.value());
        usersService.createCustomer(record.value());
    }

    @KafkaListener(topicPattern = KafkaTopics.KAFKA_KYC_IMAGE_UPLOAD_TOPIC)
    @Override
    public void uploadImage(ConsumerRecord<String, UploadImageDto> record) {
        log.info("Received UploadImageDto event: {}", record.value());
        idDetailsService.uploadImage(record.value());
    }
}
