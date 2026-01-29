package org.meristem.oneapp.usersservice;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.meristem.oneapp.usersservice.constants.AppConstants;
import org.meristem.oneapp.usersservice.constants.KafkaTopics;
import org.meristem.oneapp.usersservice.models.AmlResult;
import org.meristem.oneapp.usersservice.models.AmlSearch;
import org.meristem.oneapp.usersservice.repositories.AmlResultRepository;
import org.meristem.oneapp.usersservice.repositories.CustomRepository;
import org.meristem.oneapp.usersservice.utils.AppUtil;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import static java.util.Objects.requireNonNull;

@Slf4j
@EnableKafka
@SpringBootApplication
@EnableAsync
@EnableCaching
@ConfigurationPropertiesScan
@EnableScheduling
@EnableSchedulerLock(defaultLockAtMostFor = "5m")  // Max lock duration of 5 minutes
public class UsersServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UsersServiceApplication.class, args);
    }

    @Bean
    @Profile("!prod")
    public CommandLineRunner warmUp(CacheManager cacheManager, KafkaTemplate<String, String> kafkaTemplate, AmlResultRepository amlResultRepository, CustomRepository customRepository) {

        AmlSearch search = AmlSearch.builder()
                .searchType("")
                .subjectReference("")
                .vendor("")
                .build();
        customRepository.save(search);
        ObjectNode jsonNode = AppUtil.getMapper().createObjectNode();
        jsonNode.put("name", "Okah");
        jsonNode.put("age", "22");
        amlResultRepository.save(AmlResult.builder()
                        .aliases("obi,dave")
                        .name("Man dem")
                        .dobs("1999")
                        .countries("Nigeria,USA")
                        .position(jsonNode)
                        .vendorId("jkjka")
                        .education(jsonNode)
                        .searchId(search.getId())
                        .confidenceScore(100)

                .build());
        log.info("All {}", amlResultRepository.findAll());
        return args -> {
            try {
                requireNonNull(cacheManager.getCache(AppConstants.USERS_CACHE_NAME)).clear();
                requireNonNull(cacheManager.getCache(AppConstants.SIGN_UP_CACHE_NAME)).clear();
                kafkaTemplate.send(KafkaTopics.KAFKA_HEALTH_TOPIC, "ping");
            } catch (Exception e) {
                log.error("Failed to send Kafka health ping or clear redis cache", e);
            }
        };
    }
}
