package org.meristem.oneapp.walletservice.constants;


import lombok.experimental.UtilityClass;

@UtilityClass
public final class KafkaTopics {

    public static final String KAFKA_USER_CREATED = "user.created.topic";
    public static final String KAFKA_KYC_COMPLETED = "kyc.completed.topic";
    public static final String KAFKA_HEALTH_TOPIC = "health";

}
