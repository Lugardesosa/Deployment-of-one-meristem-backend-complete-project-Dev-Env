package org.meristem.oneapp.reportservice.services;


import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.meristem.oneapp.kafka.dtos.ActivityLogEventDto;
import org.meristem.oneapp.kafka.dtos.TransactionEventDto;

public interface IKafkaListeners {

    void consumeTransactions(ConsumerRecord<String, TransactionEventDto> record);

    void consumeActivityLogs(ConsumerRecord<String, ActivityLogEventDto> record);
}
