package org.meristem.oneapp.usersservice.utils;

import lombok.experimental.UtilityClass;
import org.meristem.oneapp.usersservice.models.OtpVerification;

@UtilityClass
public final class AppUtil {

    public static int randomInt(int min, int max) {
        return (int) (Math.random() * (max - min) + min);
    }
}
