package org.meristem.oneapp.coreservices.wealth.constants;


import lombok.experimental.UtilityClass;


@UtilityClass
public final class KafkaTopics {

    // use dot (.) as separator
    public static final String KAFKA_HEALTH_TOPIC = "health";
    public static final String KAFKA_TRANSACTIONS_TOPIC = "transactions.event";
    public static final String KAFKA_ACTIVITY_LOG_TOPIC = "activity_log.event";
}
