package org.meristem.oneapp.wealthservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
@ConfigurationPropertiesScan({"org.meristem.oneapp.wealthservice.config.configProperties"})
public class WealthServiceApplication {

    static void main(String[] args) {
        SpringApplication.run(WealthServiceApplication.class, args);
    }

}
