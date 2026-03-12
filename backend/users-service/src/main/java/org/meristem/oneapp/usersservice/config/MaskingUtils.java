package org.meristem.oneapp.usersservice.config;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class MaskingUtils {


    private static String maskCore(String s) {
        if (s == null || s.isEmpty()) return s;
        int len = s.length();

        if (len <= 2) return "*" + s + "*";
        if (len <= 4) {
            return s.charAt(0) + String.valueOf('*').repeat(len - 2) + s.charAt(len - 1);
        }

        return s.substring(0, 2)
                + String.valueOf('*').repeat(len - 4)
                + s.substring(len - 2);
    }

    public static String maskFirstName(String firstName) {
        return maskCore(firstName);
    }

    public static String maskMiddleName(String middleName) {
        return maskCore(middleName);
    }

    public static String maskLastName(String lastName) {
        return maskCore(lastName);
    }

    public static String maskEmail(String email) {
        if (email == null || email.isBlank() || !email.contains("@")) return email;

        String[] parts = email.split("@", 2);
        String local = parts[0];
        String domain = parts[1];

        String maskedLocal = maskCore(local);

        int dot = domain.indexOf('.');
        if (dot <= 0) return maskedLocal + "@" + maskCore(domain);

        String domainName = domain.substring(0, dot);
        String tldAndRest = domain.substring(dot);
        return maskedLocal + "@" + maskCore(domainName) + tldAndRest;
    }

    public static String maskPhone(String phone) {
        if (phone == null || phone.isBlank()) return phone;

        String digitsOnly = phone.replaceAll("\\D", "");
        if (digitsOnly.isEmpty()) return phone;

        String maskedDigits = maskCore(digitsOnly);

        StringBuilder out = new StringBuilder();
        int di = 0;
        for (char c : phone.toCharArray()) {
            if (Character.isDigit(c)) {
                out.append(maskedDigits.charAt(di++));
            } else {
                out.append(c);
            }
        }
        return out.toString();
    }
}
