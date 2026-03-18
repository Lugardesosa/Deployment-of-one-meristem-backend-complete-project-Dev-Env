package org.meristem.oneapp.walletservice.services.implementations;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.meristem.oneapp.kafka.dtos.UserCreatedDto;
import org.meristem.oneapp.walletservice.constants.KafkaTopics;
import org.meristem.oneapp.walletservice.services.IKafkaListeners;
import org.meristem.oneapp.walletservice.services.IWalletAccountService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaListeners implements IKafkaListeners {

    private final IWalletAccountService iWalletService;

    @KafkaListener(topicPattern = KafkaTopics.KAFKA_WALLET_CREATE_TOPIC)
    @Transactional
    @Override
    public void listenKycCompleted(ConsumerRecord<String, UserCreatedDto> record) {
        log.info("Received UserCreatedDto event: {}", record.value());
        iWalletService.create(record.value());
    }
}
