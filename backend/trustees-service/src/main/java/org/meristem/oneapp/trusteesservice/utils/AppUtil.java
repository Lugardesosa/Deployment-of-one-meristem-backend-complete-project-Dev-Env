package org.meristem.oneapp.trusteesservice.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.meristem.oneapp.trusteesservice.domains.enums.Roles;
import org.meristem.oneapp.trusteesservice.exception.exceptions.BadRequestException;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.data.util.Pair;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.util.StringUtils;

import javax.crypto.Mac;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

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
        return "SYSTEM.AUTO";
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

    public static Mac getHmacSHA256() throws NoSuchAlgorithmException {
        return Mac.getInstance("HmacSHA256");
    }

    public static boolean nonIsNull(Object... s) {
        return Arrays.stream(s).allMatch(Objects::nonNull);
    }

    public static boolean isValidDateRage(OffsetDateTime from, Integer daysRange) {
        return from.isAfter(OffsetDateTime.now().minusDays(daysRange));
    }

    public static boolean isValidDateRage(LocalDateTime from, Integer daysRange) {
        return from.isAfter(LocalDateTime.now().minusDays(daysRange));
    }

    public static String getServiceUrl(List<ServiceInstance> instances, Pair<String, String> service) {

        return isNull(instances) || instances.isEmpty() ? service.getSecond() :
                instances.getFirst().getUri().toString().concat(instances.getFirst().getMetadata().getOrDefault("contextPath", ""));
    }

    public static String _upperCaseToTitleCase(String str) {
        return String.join(" ", Arrays.stream(str.toLowerCase().split("_"))
                .map(StringUtils::capitalize).toList());
    }

    public static Boolean isAdmin() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof JwtAuthenticationToken authenticationToken) {
            Jwt jwt = (Jwt) authenticationToken.getPrincipal();
            Boolean isAdmin = jwt.getClaimAsBoolean("isAdmin");
            if (isAdmin == null) return false;
            return isAdmin;
        }
        return false;
    }
}
