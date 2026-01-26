package org.meristem.oneapp.reportservice.services.implementations;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.meristem.oneapp.kafka.dtos.ActivityLogEventDto;
import org.meristem.oneapp.kafka.dtos.TransactionEventDto;
import org.meristem.oneapp.reportservice.constants.KafkaTopics;
import org.meristem.oneapp.reportservice.services.IActivityLogService;
import org.meristem.oneapp.reportservice.services.IKafkaListeners;
import org.meristem.oneapp.reportservice.services.ITransactionsService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class KafkaListeners implements IKafkaListeners {

    private final ITransactionsService transactionsService;
    private final IActivityLogService activityLogService;

    @Transactional
    @KafkaListener(topicPattern = KafkaTopics.KAFKA_TRANSACTIONS_TOPIC)
    public void consumeTransactions(ConsumerRecord<String, TransactionEventDto> record) {
        transactionsService.saveTransactions(record.value());
    }


    @Transactional
    @KafkaListener(topicPattern = KafkaTopics.KAFKA_ACTIVITY_LOG_TOPIC)
    public void consumeActivityLogs(ConsumerRecord<String, ActivityLogEventDto> record) {
        activityLogService.saveActivityLogs(record.value());
    }
}
