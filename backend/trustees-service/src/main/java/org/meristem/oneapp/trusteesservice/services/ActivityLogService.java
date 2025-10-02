package org.meristem.oneapp.trusteesservice.services;


import lombok.RequiredArgsConstructor;
import org.meristem.oneapp.kafka.dtos.ActivityLogEventDto;
import org.meristem.oneapp.trusteesservice.config.configProperties.TrusteesServiceProperties;
import org.meristem.oneapp.trusteesservice.constants.KafkaTopics;
import org.meristem.oneapp.trusteesservice.domains.enums.ActivityLogType;
import org.meristem.oneapp.trusteesservice.utils.AppUtil;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ActivityLogService {

    private final KafkaSenderService kafkaSenderService;
    private final TrusteesServiceProperties trusteesServiceProperties;

    public  <T> void sendActivity(Class<T> tClass, ActivityLogType activityLogType, Long entityId, Map<String, Object> metadata, String activity) {
        ActivityLogEventDto activityLogEventDto = ActivityLogEventDto.builder()
                .actor(AppUtil.getLoggedInUserEmail())
                .activity(activity)
                .activityDate(LocalDateTime.now()).action(activityLogType.getAction())
                .entityId(entityId).entity(tClass.getSimpleName())
                .metadata(metadata).application(trusteesServiceProperties.applicationName())
                .build();
        kafkaSenderService.send(activityLogEventDto, Map.of(KafkaHeaders.TOPIC, KafkaTopics.KAFKA_ACTIVITY_LOG_TOPIC));
    }
}
