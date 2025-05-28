package org.meristem.oneapp.usersservice;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.KafkaAdminClient;
import org.meristem.oneapp.usersservice.constants.AppConstants;
import org.meristem.oneapp.usersservice.utils.AppUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestClient;

import static java.util.Objects.requireNonNull;

@Slf4j
@EnableKafka
@SpringBootApplication
@EnableAsync
@EnableCaching
@EnableConfigurationProperties
@ConfigurationPropertiesScan
@RequiredArgsConstructor
public class UsersServiceApplication {

    private final CacheManager cacheManager;

    public static void main(String[] args) {
        SpringApplication.run(UsersServiceApplication.class, args);
    }

    @PostConstruct
    public void warmUp() {
        try {
            requireNonNull(cacheManager.getCache(AppConstants.USERS_CACHE_NAME)).clear();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
