package org.meristem.oneapp.trusteesservice.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class AppConstants {

    public static final Integer DAYS_RANGE = 365;
    public static final Integer MAX_RETRY_ATTEMPTS = 3;
    public static final long HTTP_RETRY_DELAY = 800L;
    public static int PAGE_SIZE = 20;
    public static int SORT_ORDER = 0; // 0 for descending and 1 for ascending
    public static String FORM_TYPE_ASSET = "ASSET";
    public static String FORM_TYPE_PLAN = "PLAN";
    public static final String EMAIL_REGEX_PATTERN = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
    public static final String APPLICATION_JSON_UTF8_VALUE = "application/json;charset=UTF-8";

}
