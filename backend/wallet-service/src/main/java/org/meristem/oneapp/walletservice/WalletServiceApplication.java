package org.meristem.oneapp.walletservice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.meristem.oneapp.walletservice.constants.KafkaTopics;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@EnableScheduling
@EnableAsync
@ConfigurationPropertiesScan
@SpringBootApplication
@RequiredArgsConstructor
@EnableSchedulerLock(defaultLockAtMostFor = "5m")  // Max lock duration of 5 minutes
@Slf4j
public class WalletServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(WalletServiceApplication.class, args);
    }

    @Bean
    public CommandLineRunner warmUp(KafkaTemplate<String, String> kafkaTemplate) {

        return args -> {

            try {
                kafkaTemplate.send(KafkaTopics.KAFKA_HEALTH_TOPIC, "ping");
            } catch (KafkaException e) {
                log.error("Failed to send Kafka health ping ", e);
            }
        };
    }
}
