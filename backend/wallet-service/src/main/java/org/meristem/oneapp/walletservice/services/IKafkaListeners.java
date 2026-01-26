package org.meristem.oneapp.walletservice.services;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.meristem.oneapp.kafka.dtos.KycCompletedDto;

public interface IKafkaListeners {
    void listenKycCompleted(ConsumerRecord<String, KycCompletedDto> record);
}
