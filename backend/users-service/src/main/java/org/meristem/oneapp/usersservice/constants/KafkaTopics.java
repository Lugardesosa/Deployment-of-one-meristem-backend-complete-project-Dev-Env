package org.meristem.oneapp.usersservice.constants;


import lombok.experimental.UtilityClass;

@UtilityClass
public final class KafkaTopics {
    public static final String KAFKA_OTP_TOPIC = "otp.topic";
    public static final String KAFKA_SMILE_ID_TOPIC = "smile.id.topic";
    public static final String KAFKA_OTP_VERIFIED_TOPIC = "otp.verified.topic";
    public static final String KAFKA_LOGIN_TOPIC = "login.topic";
    public static final String KAFKA_SUCCESSFUL_PASSWORD_RESET = "password.reset.topic";
    public static final String KAFKA_USER_CREATED = "user.created.topic";
    public static final String ADMIN_ACCOUNT_CREATED = "admin.account.created";
    public static final String KAFKA_HEALTH_TOPIC = "health";
    public static final String KAFKA_KYC_COMPLETED = "kyc.completed.topic";
    public static final String KAFKA_KYC_REJECTED = "kyc.rejected.topic";

    public static final String KAFKA_KYC_IMAGE_UPLOAD_TOPIC = "kyc.image.upload.topic";
    public static final String KAFKA_KYC_CUSTOMER_CREATE_TOPIC = "kyc.customer.create.topic";
}
