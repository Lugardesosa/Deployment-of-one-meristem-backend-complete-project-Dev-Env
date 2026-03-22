package org.meristem.oneapp.coreservices;

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
public class CoreServicesApplication {

    static void main(String[] args) {
        SpringApplication.run(CoreServicesApplication.class, args);
    }

}
