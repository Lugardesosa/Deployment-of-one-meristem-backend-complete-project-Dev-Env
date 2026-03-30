package org.meristem.oneapp.walletservice.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class AppConstants {
    public static final Integer MAX_RETRY_ATTEMPTS = 3;
    public static final long HTTP_RETRY_DELAY = 800L;
    public static final String APPLICATION_JSON_UTF8_VALUE = "application/json;charset=UTF-8";

    public static final int OUTBOX_MAX_RETRY_COUNT = 3;
    public static final String BANK_ACCOUNT = "BANK_ACCOUNT";
}
