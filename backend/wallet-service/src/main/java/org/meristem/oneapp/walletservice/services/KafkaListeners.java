package org.meristem.oneapp.walletservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.meristem.oneapp.kafka.dtos.KycCompletedDto;
import org.meristem.oneapp.walletservice.constants.KafkaTopics;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaListeners {

    private final VirtualAccountService virtualAccountService;

    @KafkaListener(topicPattern = KafkaTopics.KAFKA_KYC_COMPLETED)
    @Transactional
    public void listenKycCompleted(ConsumerRecord<String, KycCompletedDto> record) {
        log.info("Received KycCompleted event: {}", record.value());
        virtualAccountService.createVirtualAccounts(record.value());
    }
}
