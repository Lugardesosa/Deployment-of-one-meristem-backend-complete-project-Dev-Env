package org.meristem.oneapp.usersservice.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class ErrorMessages {

    public static final String INVALID_USERNAME = "Invalid username";
    public static final String INVALID_PASSWORD = "Invalid password";
    public static final String TOKEN_COULD_NOT_BE_GENERATED = "Token could not be generated";
    public static final String REFRESH_TOKEN_COULD_NOT_BE_GENERATED = "Refresh token could not be generated";
    public static final String METHOD_NOT_SUPPORTED = "Http Request Method Not Supported";
    public static final String NO_RESOURCE_FOUND = "No Resource Found";
    public static final String MEDIA_TYPES_NOT_SUPPORTED = "Http MediaType Not Supported";
    public static final String MEDIA_TYPES_NOT_ACCEPTABLE = "Http MediaType Not Acceptable";
    public static final String MISSING_PATH_VARIABLE = "Missing Path Variable";
    public static final String MAX_UPLOAD_SIZE_EXCEEDED = "Max Upload Size Exceeded";
}
