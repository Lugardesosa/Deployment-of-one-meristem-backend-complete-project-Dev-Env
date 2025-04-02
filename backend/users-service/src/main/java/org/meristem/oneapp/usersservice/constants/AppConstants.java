package org.meristem.oneapp.usersservice.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class AppConstants {

    public static final short OTP_EXPIRES_AT_MINUTES = 10;

    public static String EMAIL_REGEX_PATTERN = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
    public static String PHONE_NG_REGEX_PATTERN = "^(?:\\+234|234|0)(70[1-9]|80[2-9]|81[0-9]|90[1-9]|91[0-6])\\d{7}$";
//    String regex = "^(?:\\+234|234|0)(70[1-9]|80[2-9]|81[0-9]|90[1-9]|91[0-6]|701|702|703|704|705|706|707|708|709|802|803|804|805|806|807|808|809|810|811|812|813|814|815|816|817|818|819|901|902|903|904|905|906|907|908|909|911|912|913|914|915|916|917|918|919)\\d{6}$";
    public static String PASSWORD_REGEX_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,20}$";

    public static String KAFKA_OTP_TOPIC = "otp-topic";
}
