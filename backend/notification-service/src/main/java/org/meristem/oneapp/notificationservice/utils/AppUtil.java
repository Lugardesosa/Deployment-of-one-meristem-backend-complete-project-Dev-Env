package org.meristem.oneapp.notificationservice.utils;

import lombok.experimental.UtilityClass;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Random;


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
}
