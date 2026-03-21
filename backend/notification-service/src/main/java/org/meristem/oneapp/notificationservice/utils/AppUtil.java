package org.meristem.oneapp.notificationservice.utils;

import lombok.experimental.UtilityClass;
import org.meristem.oneapp.notificationservice.exception.exceptions.BadRequestException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@UtilityClass
public final class AppUtil {

    public static String sha215Hash(String input) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] salt = new byte[16];
        SecureRandom.getInstanceStrong().nextBytes(salt);
        digest.update(salt);
        byte[] hash = digest.digest(input.getBytes());
        return Base64.getEncoder().encodeToString(hash);
    }

    public static String generatePassword(int size) {
        Random rand = new Random();

        Character[] alphaNumeral = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
                'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        char[] password = new char[size];
        for (int i = 0; i < size; i++) {
            password[i] = alphaNumeral[rand.nextInt(alphaNumeral.length)];
        }
        return new String(password);
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

    public static Boolean isAuthenticated() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.isAuthenticated();
    }

    public static Long getAuthUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof JwtAuthenticationToken authenticationToken) {
            Jwt jwt = (Jwt) authenticationToken.getPrincipal();
            return jwt.getClaim("id");
        }
        return null;
    }

    public static String getLoggedInUserEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof JwtAuthenticationToken authenticationToken) {
            Jwt jwt = (Jwt) authenticationToken.getPrincipal();
            return jwt.getClaim("email").toString();
        }
        return "SYSTEM.AUTO";
    }


    public static String extractExpoTokenWithRegex(String input) {
        Pattern pattern = Pattern.compile("ExponentPushToken\\[[^]]+]");
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            return matcher.group(0);
        }
        return null;
    }
}
