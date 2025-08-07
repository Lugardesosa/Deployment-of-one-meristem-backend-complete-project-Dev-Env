package org.meristem.oneapp.walletservice.utils;

import lombok.experimental.UtilityClass;
import org.meristem.oneapp.walletservice.domains.enums.AccountProvider;
import org.meristem.oneapp.walletservice.exception.exceptions.BadRequestException;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static java.util.Objects.nonNull;

@UtilityClass
public final class AppUtil {

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

    public static String generateTransactionReference(Long walletVirtualId) {

        final int RANDOM_LENGTH = 6;
        final String PREFIX = "MER";
        final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

        String timestamp = LocalDateTime.now().format(FORMATTER);
        String randomPart = randomAlphanumeric(RANDOM_LENGTH);
        return String.format("%s-%s-%s-%s", PREFIX, timestamp, walletVirtualId, randomPart);
    }

    private static String randomAlphanumeric(int length) {
        final SecureRandom RANDOM = new SecureRandom();
        final char[] ALPHANUM = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(ALPHANUM[RANDOM.nextInt(ALPHANUM.length)]);
        }
        return sb.toString();
    }

    public static String generateVirtualAccountReference(AccountProvider provider, Long userId) {

        final int RANDOM_LENGTH = 4;
        final String PREFIX = "MER";

        String randomPart = randomAlphanumeric(RANDOM_LENGTH);
        return String.format("%s-%s-%s-%s", PREFIX, provider.getValue().substring(Integer.min(provider.getValue().length(), 4)), userId, randomPart);
    }

    public static String getUserFullName(String firstName, String middleName, String lastName) {
        return firstName + " " + (nonNull(middleName) ? (middleName + " "): "") + lastName;
    }

    public static LocalDateTime nonNullOrLocalDateTimeNow(LocalDateTime localDateTime) {
        return nonNull(localDateTime) ? localDateTime : LocalDateTime.now();
    }

    public static String getServiceUrl(List<ServiceInstance> instances, String serviceName) {

        if (instances.isEmpty()) {
            throw new IllegalStateException("No instances found for " + serviceName);
        }

        return instances.getFirst().getUri().toString().concat(instances.getFirst().getMetadata().getOrDefault("contextPath", ""));
    }
}
