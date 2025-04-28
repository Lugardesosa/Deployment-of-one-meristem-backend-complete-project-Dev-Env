package org.meristem.oneapp.usersservice.constants;

import lombok.experimental.UtilityClass;
import org.springframework.data.util.Pair;

@UtilityClass
public final class AppConstants {

    public static final short OTP_EXPIRES_AT_MINUTES = 5;

    public static final String NAME_REGEX = "^[a-zA-Z]{0,150}$";
    public static final String URL_REGEX_PATTERN = "^https?:\\/\\/(?:www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,1000}\\.[a-zA-Z0-9()]{1,20}\\b(?:[-a-zA-Z0-9()@:%_\\+.~#?&\\/=]*)$";
    public static final String EMAIL_REGEX_PATTERN = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
    public static final String PHONE_NG_REGEX_PATTERN = "^(?:\\+234|234|0)(70[1-9]|80[2-9]|81[0-9]|90[1-9]|91[0-6])\\d{7}$";
    public static final String PASSWORD_REGEX_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,20}$";

    public static final String KAFKA_OTP_TOPIC = "otp-topic";
    public static final Pair<Integer, Integer> fourNumbersOtp = Pair.of(1000, 9000);
    public static final Pair<Integer, Integer> fiveNumbersOtp = Pair.of(10000, 90000);
    public static final Pair<Integer, Integer> sixNumbersOtp = Pair.of(100000, 900000);
    public static final Pair<Integer, Integer> sevenNumbersOtp = Pair.of(1000000, 9000000);
}
