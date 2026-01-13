package org.meristem.oneapp.trusteesservice;

import lombok.extern.slf4j.Slf4j;
import org.meristem.oneapp.trusteesservice.constants.KafkaTopics;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableAsync;


@Slf4j
@EnableKafka
@EnableAsync
@EnableCaching
@ConfigurationPropertiesScan
@SpringBootApplication
public class TrusteesServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TrusteesServiceApplication.class, args);
    }

    @Bean
    public CommandLineRunner warmUp(KafkaTemplate<String, String> kafkaTemplate) {

        return args -> {
            try {
                kafkaTemplate.send(KafkaTopics.KAFKA_HEALTH_TOPIC, "ping");
            } catch (Exception e) {
                log.error("Failed to send Kafka health ping ", e);
            }
        };
    }

}
