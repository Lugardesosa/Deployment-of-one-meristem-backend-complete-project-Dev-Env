package org.meristem.oneapp.coreservices.notifications.services;

import java.util.Map;

public interface IKafkaSenderService {
    void send(String topic, Object payload);
    void send(Object payload, Map<String, Object> headers);
    void send(String topic, String key, Object payload);
    void send(String topic, Integer partition, String key, Object payload);
    void send(String topic, Integer partition, Long timeStamp, String key, Object payload);
}
