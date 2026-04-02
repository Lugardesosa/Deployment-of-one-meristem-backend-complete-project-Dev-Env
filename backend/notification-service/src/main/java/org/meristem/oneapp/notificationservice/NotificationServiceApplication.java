package org.meristem.oneapp.notificationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;


@ConfigurationPropertiesScan
@EnableKafka
@EnableAsync
@SpringBootApplication
@EnableScheduling
public class NotificationServiceApplication {
    static void main(String[] args) {
        SpringApplication.run(NotificationServiceApplication.class, args);
    }

}
