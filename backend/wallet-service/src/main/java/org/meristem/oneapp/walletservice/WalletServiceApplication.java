package org.meristem.oneapp.walletservice;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.meristem.oneapp.walletservice.constants.KafkaTopics;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.kafka.core.KafkaTemplate;


@ConfigurationPropertiesScan
@SpringBootApplication
@RequiredArgsConstructor
@Slf4j
public class WalletServiceApplication {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public static void main(String[] args) {
        SpringApplication.run(WalletServiceApplication.class, args);
    }

    @PostConstruct
    public void warmUp() {
        try {
            kafkaTemplate.send(KafkaTopics.KAFKA_HEALTH_TOPIC, "ping");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
