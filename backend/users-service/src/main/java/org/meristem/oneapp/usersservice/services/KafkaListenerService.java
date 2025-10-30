package org.meristem.oneapp.usersservice.services;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.meristem.oneapp.kafka.dtos.OtpVerifiedDto;
import org.meristem.oneapp.usersservice.constants.KafkaTopics;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class KafkaListenerService {

    private final UsersService usersService;

    /**
     * Kafka listener for OTP verification events.
     * Consumes messages from the topic pattern defined in {@link org.meristem.oneapp.usersservice.constants.KafkaTopics#KAFKA_OTP_VERIFIED_TOPIC}
     * and delegates processing to {@link org.meristem.oneapp.usersservice.services.UsersService#emailVerified(org.meristem.oneapp.kafka.dtos.OtpVerifiedDto)}.
     *
     * @param record the consumed Kafka record containing the message key and an {@link org.meristem.oneapp.kafka.dtos.OtpVerifiedDto} payload
     */
    @KafkaListener(topicPattern = KafkaTopics.KAFKA_OTP_VERIFIED_TOPIC)
    public void otpVerified(ConsumerRecord<String, OtpVerifiedDto> record) {
        usersService.emailVerified(record.value());
    }
}
