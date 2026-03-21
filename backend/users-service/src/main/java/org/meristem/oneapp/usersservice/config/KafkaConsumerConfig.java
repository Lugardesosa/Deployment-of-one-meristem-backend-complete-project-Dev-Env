package org.meristem.oneapp.usersservice.config;

import org.springframework.boot.autoconfigure.kafka.DefaultKafkaConsumerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

@Configuration
public class KafkaConsumerConfig {

    @Bean
    public DefaultKafkaConsumerFactoryCustomizer consumerFactoryCustomizer() {
        return consumerFactory -> {
            consumerFactory.updateConfigs(java.util.Map.of(
                    JsonDeserializer.TRUSTED_PACKAGES, "java.util,java.lang,org.meristem.oneapp.kafka.*,org.meristem.oneapp.usersservice.dtos.events.*"
            ));
        };
    }
}
