package org.meristem.oneapp.walletservice.services;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.meristem.oneapp.kafka.dtos.KycCompletedDto;
import org.meristem.oneapp.kafka.dtos.UserCreatedDto;
import org.meristem.oneapp.walletservice.constants.KafkaTopics;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.transaction.annotation.Transactional;

public interface IKafkaListeners {

    void listenKycCompleted(ConsumerRecord<String, UserCreatedDto> record);
}
