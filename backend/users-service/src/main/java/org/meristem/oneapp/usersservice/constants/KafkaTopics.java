package org.meristem.oneapp.usersservice.constants;


import lombok.experimental.UtilityClass;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@UtilityClass
public final class KafkaTopics {
    public static final String KAFKA_OTP_TOPIC = "otp.topic";
    public static final String KAFKA_SUCCESSFUL_PASSWORD_RESET = "password.reset.topic";
    public static final String KAFKA_USER_CREATED = "user.created.topic";
    public static final String ADMIN_ACCOUNT_CREATED = "admin.account.created";
    public static final String KAFKA_HEALTH_TOPIC = "health";
    public static final String KAFKA_KYC_COMPLETED = "kyc.completed.topic";
    public static final String KAFKA_KYC_REJECTED = "kyc.rejected.topic";

}
