package org.meristem.oneapp.usersservice.constants;

import lombok.experimental.UtilityClass;
import org.springframework.data.util.Pair;

@UtilityClass
public final class AppConstants {

    public static final short OTP_EXPIRES_AT_MINUTES = 5;

    public static final String NAME_REGEX = "^[a-zA-Z\\s-']{0,150}$";
    public static final String URL_REGEX_PATTERN = "^https?:\\/\\/(?:www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,1000}\\.[a-zA-Z0-9()]{1,20}\\b(?:[-a-zA-Z0-9()@:%_\\+.~#?&\\/=]*)$";
//    public static final String URL_REGEX_PATTERN = "^(https?)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
    public static final String EMAIL_REGEX_PATTERN = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
    public static final String PHONE_NG_REGEX_PATTERN = "^(?:\\+234|234|0)(70[1-9]|80[2-9]|81[0-9]|90[1-9]|91[0-6])\\d{7}$";
//    String regex = "^(?:\\+234|234|0)(70[1-9]|80[2-9]|81[0-9]|90[1-9]|91[0-6]|701|702|703|704|705|706|707|708|709|802|803|804|805|806|807|808|809|810|811|812|813|814|815|816|817|818|819|901|902|903|904|905|906|907|908|909|911|912|913|914|915|916|917|918|919)\\d{6}$";
    public static final String PASSWORD_REGEX_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,20}$";

    public static final String KAFKA_OTP_TOPIC = "otp-topic";
    public static final Pair<Integer, Integer> fiveNumbersOtp = Pair.of(10000, 90000);
    public static final Pair<Integer, Integer> sixNumbersOtp = Pair.of(100000, 900000);
    public static final Pair<Integer, Integer> sevenNumbersOtp = Pair.of(1000000, 9000000);
}
