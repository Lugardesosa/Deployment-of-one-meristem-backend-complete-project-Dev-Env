package org.meristem.oneapp.usersservice.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.meristem.oneapp.usersservice.exception.exceptions.BadRequestException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import javax.crypto.Mac;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

import static java.util.Objects.nonNull;
import static org.meristem.oneapp.usersservice.constants.AppConstants.specialChars;

@Slf4j
@UtilityClass
public final class AppUtil {

    public static int randomInt(int min, int max) {
        return (int) (Math.random() * (max - min) + min);
    }

    public static String getLoggedInSubject() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof JwtAuthenticationToken authenticationToken) {
            Jwt jwt = (Jwt) authenticationToken.getPrincipal();
            return jwt.getClaimAsString("sub");
        }
        return "SYSTEM.AUTO";
    }

    public static Long getLoggedInUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof JwtAuthenticationToken authenticationToken) {
            Jwt jwt = (Jwt) authenticationToken.getPrincipal();
            return jwt.getClaim("id");
        }
        throw new BadRequestException("User is not logged in");
    }

    public static String getLoggedInUserEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof JwtAuthenticationToken authenticationToken) {
            Jwt jwt = (Jwt) authenticationToken.getPrincipal();
            return jwt.getClaim("email").toString();
        }
        throw new BadRequestException("User is not logged in");
    }


    public static String getLoggedInUserFullName() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof JwtAuthenticationToken authenticationToken) {
            Jwt jwt = (Jwt) authenticationToken.getPrincipal();
            String firstName = nonNull(jwt.getClaim("firstName")) ? jwt.getClaim("firstName").toString() : "";
            String lastName = nonNull(jwt.getClaim("lastName")) ? jwt.getClaim("lastName").toString() : "";
            return firstName + " " + lastName;
        }
        throw new BadRequestException("User is not logged in");
    }

    public static String getLoggedInUserPhone() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof JwtAuthenticationToken authenticationToken) {
            Jwt jwt = (Jwt) authenticationToken.getPrincipal();
            return jwt.getClaim("phoneNumber").toString();
        }
        return null;
    }

    public static String generateReferralCode(String firstName) {
        Random rand = new Random();
        String[] alphabets = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
        return "MW-" +
                firstName.substring(0, Integer.min(8, firstName.length())).toUpperCase() +
                alphabets[rand.nextInt(26)] +
                rand.nextInt(10) +
                rand.nextInt(10);
    }

    public static String generatePassword(int size) {
        Random rand = new Random();

        Character[] alphabets = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
        'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
        char[] password = new char[size];
        for (int i = 0; i < size; i++) {
            password[i] = alphabets[rand.nextInt(alphabets.length)];
        }
        password[rand.nextInt(size)] = specialChars[rand.nextInt(specialChars.length)];
        password[rand.nextInt(size)] = specialChars[rand.nextInt(specialChars.length)];
        return new String(password);
    }

    public static Mac getHmacSHA256() throws NoSuchAlgorithmException {
        return Mac.getInstance("HmacSHA256");
    }

    public static boolean nonIsNull(Object... s) {
        return Arrays.stream(s).allMatch(Objects::nonNull);
    }
}
