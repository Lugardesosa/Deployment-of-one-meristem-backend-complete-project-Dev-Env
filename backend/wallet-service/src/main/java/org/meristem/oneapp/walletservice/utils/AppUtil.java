package org.meristem.oneapp.walletservice.utils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.jspecify.annotations.NonNull;
import org.meristem.oneapp.walletservice.domains.enums.AccountProvider;
import org.meristem.oneapp.walletservice.exception.exceptions.BadRequestException;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        return firstName + " " + (nonNull(middleName) ? (middleName + " ") : "") + lastName;
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

    public static String titleCase(String text) {

        if (text == null)
            return null;

        Pattern pattern = Pattern.compile("\\b([a-zÀ-ÖØ-öø-ÿ])([\\w]*)");
        Matcher matcher = pattern.matcher(text.toLowerCase());

        StringBuilder buffer = new StringBuilder();

        while (matcher.find())
            matcher.appendReplacement(buffer, matcher.group(1).toUpperCase() + matcher.group(2));

        return matcher.appendTail(buffer).toString();
    }

    public static BigDecimal generateRandomBigDecimalFromRange(BigDecimal min, BigDecimal max, int scale) {
        BigDecimal range = max.subtract(min);
        BigDecimal randomBigDecimal = min.add(range.multiply(new BigDecimal(Math.random())));
        return randomBigDecimal.setScale(scale, RoundingMode.HALF_EVEN);
    }

    public static Long getInvestmentId(HttpServletRequest request) {
        try {
            return Long.valueOf(request.getHeader("SUBSIDIARY_ID"));
        } catch (NumberFormatException e) {
            throw new BadRequestException("Kindly pass SUBSIDIARY_ID in the Header");
        }
    }

    public static @NonNull String getCustomerId(HttpServletRequest request) {
        String id = request.getHeader("CUSTOMER_ID");
        if (StringUtils.isBlank(id)) {
            throw new BadRequestException("Kindly pass CUSTOMER_ID in the Header");
        }
        return request.getHeader("CUSTOMER_ID");
    }
}
