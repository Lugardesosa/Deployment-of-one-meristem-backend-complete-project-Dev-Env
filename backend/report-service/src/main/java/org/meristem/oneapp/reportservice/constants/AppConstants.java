package org.meristem.oneapp.reportservice.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class AppConstants {

    public static final Integer MAX_RETRY_ATTEMPTS = 3;
    public static final long HTTP_RETRY_DELAY = 800L;
    public static final Integer DAYS_RANGE = 365;
    public static int PAGE_SIZE = 20;
    public static int SORT_ORDER = 0; // 0 for descending and 1 for ascending
}
