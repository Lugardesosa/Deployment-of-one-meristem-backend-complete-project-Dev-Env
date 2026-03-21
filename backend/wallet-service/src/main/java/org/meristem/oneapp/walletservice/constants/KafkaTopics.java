package org.meristem.oneapp.walletservice.constants;


import lombok.experimental.UtilityClass;

@UtilityClass
public final class KafkaTopics {

    public static final String KAFKA_USER_CREATED = "user.created.topic";
    public static final String KAFKA_KYC_COMPLETED = "kyc.completed.topic";
    public static final String KAFKA_HEALTH_TOPIC = "health";
    public static final String KAFKA_TRANSACTIONS_TOPIC = "transactions.event";
    public static final String KAFKA_WEB_SOCKET_TOPIC = "websocket.topic";
    public static final String KAFKA_WALLET_CREATE_TOPIC = "wallet.create.topic";
    public static final String KAFKA_PUSH_NOTIFICATION_TOPIC = "push.notification.topic";
}
