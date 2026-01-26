package org.meristem.oneapp.usersservice.constants;

import lombok.experimental.UtilityClass;
import org.springframework.data.util.Pair;

import java.util.List;

@UtilityClass
public final class AppConstants {

    public static final int ADDRESS_APPROVAL_TIME_IN_HOURS = 24;
    public static final int OTP_EXPIRES_AT_MINUTES = 5;
    public static final int PASSWORD_ATTEMPTS = 3;
    public static final int PAGE_SIZE = 20;
    public static final String YYYY_MM_DD = "yyyy-MM-dd";

    public static final int ADMIN_PASSWORD_LENGTH = 10;
    public static final char[] specialChars = {'@', '#', '$', '%', '^', '&', '+', '=', '(', ')', '\''};
    public static final String NAME_REGEX = "^[a-zA-Z']{0,150}$";
    public static final String URL_REGEX_PATTERN = "^https?:\\/\\/(?:www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,1000}\\.[a-zA-Z0-9()]{1,20}\\b(?:[-a-zA-Z0-9()@:%_\\+.~#?&\\/=]*)$";
    public static final String EMAIL_REGEX_PATTERN = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
    public static final String PHONE_NG_REGEX_PATTERN = "^(?:\\+234|234|0)(70[1-9]|80[2-9]|81[0-9]|90[1-9]|91[0-6])\\d{7}$";
    public static final String PASSWORD_REGEX_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[" + new String(specialChars) + "])(?=\\S+$).{8,20}$";
    public static final String PIN_REGEX_PATTERN = "^(?!(.)\\1{3})(?!0123|1234|2345|3456|4567|5678|6789|7890|0987|9876|8765|7654|6543|5432|4321|3210)\\d{4}$";

    public static final Pair<Integer, Integer> fourNumbersOtp = Pair.of(1000, 9000);
    public static final Pair<Integer, Integer> fiveNumbersOtp = Pair.of(10000, 90000);
    public static final Pair<Integer, Integer> sixNumbersOtp = Pair.of(100000, 900000);
    public static final Pair<Integer, Integer> sevenNumbersOtp = Pair.of(1000000, 9000000);

    public static final String USERS_CACHE_NAME = "users";
    public static final Integer IS_UPDATE_PIN = 1;
    public static final Integer IS_NEW_PIN = 0;
    public static final String RE_PASSWORD = "password";
    public static final String DATE_REGEX = "[0-9]{4}-[0-9]{2}-[0-9]{2}";
    public static final String APPLICATION_JSON_UTF8_VALUE = "application/json;charset=UTF-8";
    public static final String AVATAR_CACHE_NAME = "avatars";
    public static final String SIGN_UP_CACHE_NAME = "sign-ups";
    public static final String SETTINGS_CACHE_NAME = "settings";
    public static final String USER_ROLE = "USER";
    public static final List<String> MOBILE_N_WEB_ROLES = List.of("user.read", "user.write", "send_otp", "verify_otp", "create_user", "users.get",
            "password_reset", "device.register", "users.email.update", "id.query", "users.onboarding.stage");
    public static final Integer MAX_PIN_FAILED_ATTEMPTS_B4_LOCK = 5;
    public static final long PIN_LOCKED_MAX_TIME_IN_MINS = 20;
}
