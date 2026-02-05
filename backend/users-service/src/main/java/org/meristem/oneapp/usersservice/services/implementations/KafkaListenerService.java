package org.meristem.oneapp.usersservice.services.implementations;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.meristem.oneapp.kafka.dtos.KycCompletedDto;
import org.meristem.oneapp.usersservice.constants.KafkaTopics;
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
    @KafkaListener(topicPattern = KafkaTopics.KAFKA_KYC_COMPLETED)
    @Transactional
    @Override
    public void listenKycCompleted(ConsumerRecord<String, KycCompletedDto> record) {
        log.info("Received KycCompleted event: {}", record.value());
        amlService.performAmlRequest(record.value());
    }
}
