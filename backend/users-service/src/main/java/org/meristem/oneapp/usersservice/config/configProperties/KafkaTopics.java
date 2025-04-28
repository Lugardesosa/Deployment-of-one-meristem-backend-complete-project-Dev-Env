package org.meristem.oneapp.usersservice.config.configProperties;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ConfigurationProperties(prefix = "oneapp")
public class KafkaTopics {

    private Map<String, String> kafkaTopics;
}
