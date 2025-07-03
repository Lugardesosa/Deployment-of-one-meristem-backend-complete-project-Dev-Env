package org.meristem.oneapp.reportservice.constants;


import lombok.experimental.UtilityClass;


@UtilityClass
public final class KafkaTopics {

    // use dot (.) as separator
    public static final String KAFKA_HEALTH_TOPIC = "health";
    public static final String KAFKA_TRANSACTIONS_TOPIC = "transactions.event";
}
