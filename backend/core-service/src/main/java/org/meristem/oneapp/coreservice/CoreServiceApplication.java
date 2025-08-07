package org.meristem.oneapp.coreservice;

import lombok.extern.slf4j.Slf4j;
import org.meristem.oneapp.coreservice.constants.KafkaTopics;
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
public class CoreServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoreServiceApplication.class, args);
    }

    @Bean
    public CommandLineRunner warmUp(KafkaTemplate<String, String> kafkaTemplate) {

        return args -> {
            try {
                kafkaTemplate.send(KafkaTopics.KAFKA_HEALTH_TOPIC, "ping");
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        };
    }

}
