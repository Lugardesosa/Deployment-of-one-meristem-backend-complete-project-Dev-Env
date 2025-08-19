package org.meristem.oneapp.trustiesservice.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class ApiConstants {

    public static final String CONTEXT_PATH = "/";
    public static final String API_DEFAULT_ERROR_MESSAGE =
            "Something went wrong. Please try again later or enter in contact with our service.";
    public static final String API_DEFAULT_REQUEST_FAILED_MESSAGE = "Request failed.";
    public static final String PLATFORM = "/platform";
    public static final String PLATFORM_API = PLATFORM + "/api/users";
    public static final String PUBLIC = "/public";
    public static final String SUCCESSFUL_MESSAGE = "Successful";
}
