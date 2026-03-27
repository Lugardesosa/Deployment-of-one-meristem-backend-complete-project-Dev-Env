package org.meristem.oneapp.usersservice.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jspecify.annotations.NonNull;
import org.meristem.oneapp.usersservice.domains.responses.UsersResponse;
import org.meristem.oneapp.usersservice.exception.exceptions.BadRequestException;
import org.meristem.oneapp.usersservice.models.UserIdDetails;
import org.meristem.oneapp.usersservice.models.Users;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import javax.crypto.Mac;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;
import static org.meristem.oneapp.usersservice.constants.AppConstants.specialChars;

@Slf4j
@UtilityClass
public final class AppUtil {

    private static final String ALGORITHM = "AES";

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

    public static Long getLoggedInUserAccountType() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof JwtAuthenticationToken authenticationToken) {
            Jwt jwt = (Jwt) authenticationToken.getPrincipal();
            return jwt.getClaim("accountType");
        }
        throw new BadRequestException("User is not logged in");
    }

    public static String getLoggedInUserEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof JwtAuthenticationToken authenticationToken) {
            Jwt jwt = (Jwt) authenticationToken.getPrincipal();
            return jwt.getClaim("sub").toString();
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

    public static String getUserFullName(Users user) {
        return user.getFirstName() + " " + (nonNull(user.getMiddleName()) ? (user.getMiddleName() + " "): "") + user.getLastName();
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

        Character[] alphaNumeral = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
        'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        char[] password = new char[size];
        for (int i = 0; i < size; i++) {
            password[i] = alphaNumeral[rand.nextInt(alphaNumeral.length)];
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

    public static String getServiceUrl(List<ServiceInstance> instances, String serviceName) {

        if (instances.isEmpty()) {
            throw new IllegalStateException("No instances found for " + serviceName);
        }

        return instances.getFirst().getUri().toString().concat(instances.getFirst().getMetadata().getOrDefault("contextPath", ""));
    }

    public static String extractIp(HttpServletRequest request) {
        String[] headerNames = {"X-Forwarded-For", "X-Real-IP", "Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_CLIENT_IP", "HTTP_X_FORWARDED_FOR" };

        for (String header : headerNames) {
            String ip = request.getHeader(header);
            if (StringUtils.isNotBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
                return ip.split(",")[0].trim();
            }
        }
        return request.getRemoteAddr();
    }

    public static String getUserAgent(HttpServletRequest request) {
        return request.getHeader("user-agent");
    }


    public static String getSmileIdTimestamp() {

        LocalDateTime localDateTime = LocalDateTime.now();

        OffsetDateTime offsetDateTime = localDateTime.atOffset(ZoneOffset.UTC);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        return offsetDateTime.format(formatter);
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

    public static ObjectMapper getMapper() {

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        JavaTimeModule module = new JavaTimeModule();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        module.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(formatter));
        mapper.registerModule(module);
        return mapper;
    }

    public static Long getInvestmentId(HttpServletRequest request) {
        try {
            return Long.valueOf(request.getHeader("X-MERISTEM-SUBSIDIARY-ID"));
        } catch (NumberFormatException e) {
            throw new BadRequestException("Kindly pass X-MERISTEM-SUBSIDIARY-ID in the Header");
        }
    }

    public static @NonNull String getCustomerId(HttpServletRequest request) {
        String id = request.getHeader("X-MERISTEM-CUSTOMER-ID");
        if (StringUtils.isBlank(id)) {
            throw new BadRequestException("Kindly pass X-MERISTEM-CUSTOMER-ID in the Header");
        }
        return id;
    }

    public static @NonNull String getPlatform(HttpServletRequest request) {
        String platform = request.getHeader("X-MERISTEM-PLATFORM");
        if (StringUtils.isBlank(platform)) {
            throw new BadRequestException("Kindly pass X-MERISTEM-PLATFORM in the Header");
        }
        return platform;
    }

    public static @NonNull List<String> getCustomerId() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof JwtAuthenticationToken authenticationToken) {
            Jwt jwt = (Jwt) authenticationToken.getPrincipal();
            return jwt.getClaim("customerIds");
        }
        throw new BadRequestException("User is not logged in");
    }

    public static String generateCscs() {
        return UUID.randomUUID().toString().substring(0, 10) + UUID.randomUUID().toString().substring(0, 10);
    }

    public static boolean firstNamesMatch(String firstName, List<String> names) {
        return names.stream().anyMatch(n -> n.equalsIgnoreCase(firstName));
    }

    public static boolean lastNamesMatch(String lastName, List<String> names) {
        return names.stream().anyMatch(n -> n.equalsIgnoreCase(lastName));

    }

    public static boolean middleNamesMatch(String middleName, List<String> names) {
        return names.stream().anyMatch(n -> n.equalsIgnoreCase(middleName));
    }

    public static boolean dobMatch(LocalDate dateOfBirth, LocalDate existingDob) {

        return AppUtil.nonIsNull(dateOfBirth, existingDob) &&
                dateOfBirth.isEqual(existingDob);
    }

    @NonNull
    public static List<String> buildNames(String firstName, String middleName, String lastName) {
        List<String> names = new ArrayList<>();
        names.add(firstName);
        names.add(middleName);
        names.add(lastName);
        return names;
    }

    public static boolean allNamesMatch(List<String> ninNames, List<String> names) {

        Set<String> caseInsensitiveSet = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        caseInsensitiveSet.addAll(ninNames);
        names.forEach(caseInsensitiveSet::remove);
        return caseInsensitiveSet.isEmpty();
    }
}
