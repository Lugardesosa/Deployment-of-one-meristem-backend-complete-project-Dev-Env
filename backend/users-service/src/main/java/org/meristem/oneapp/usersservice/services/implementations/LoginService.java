package org.meristem.oneapp.usersservice.services.implementations;


import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.extern.slf4j.Slf4j;
import nl.basjes.parse.useragent.UserAgent;
import nl.basjes.parse.useragent.UserAgentAnalyzer;
import org.meristem.oneapp.kafka.dtos.GeoIPDto;
import org.meristem.oneapp.usersservice.models.DeviceMetadata;
import org.meristem.oneapp.usersservice.models.Users;
import org.meristem.oneapp.usersservice.repositories.CustomRepository;
import org.meristem.oneapp.usersservice.services.ILoginService;
import org.meristem.oneapp.usersservice.utils.AppUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import static java.util.Objects.nonNull;

@Service
@Slf4j
public class LoginService implements ILoginService {

    private final CustomRepository customRepository;
    @Value("${one-app.users-service.geoip-filepath}")
    private String geoipDbLocation;

    private final UserAgentAnalyzer parser;

    private DatabaseReader dbReader;

    public LoginService(UserAgentAnalyzer parser, CustomRepository customRepository) throws IOException {
        this.parser = parser;
        this.customRepository = customRepository;
    }

    /**
     * Initializes the GeoIP database reader after bean construction.
     *
     * @throws IOException if the database file cannot be loaded
     */
    @PostConstruct
    public void init() throws IOException {
        File database = new File(geoipDbLocation);
        dbReader = new DatabaseReader.Builder(database).build();
    }

    /**
     * Resolves geographic information for the provided IPv4/IPv6 address.
     *
     * @param ip textual IP address to look up
     * @return a DTO containing city, country, latitude and longitude for the IP
     * @throws IOException      if the underlying database cannot be accessed
     * @throws GeoIp2Exception  if the IP cannot be resolved in the GeoIP database
     */
    public GeoIPDto getLocation(String ip)
            throws IOException, GeoIp2Exception {
        InetAddress ipAddress = InetAddress.getByName(ip);
        CityResponse response = dbReader.city(ipAddress);

        String cityName = response.getCity().getName();
        String country = response.getCountry().getName();
        String latitude = response.getLocation().getLatitude().toString();
        String longitude = response.getLocation().getLongitude().toString();
        return new GeoIPDto(ip, cityName, country, latitude, longitude);
    }

    /**
     * Persists the given device metadata record.
     *
     * @param deviceMetadata device information to store
     */
    /**
     * Persists the given device metadata record.
     *
     * @param deviceMetadata device information to store
     */
    public void saveDevice(DeviceMetadata deviceMetadata) {
        customRepository.save(deviceMetadata);
    }

    /**
     * Builds a displayable "city, country" string from the given parts.
     * Null inputs are treated as empty strings; leading commas/spaces are removed and the result is trimmed.
     *
     * @param cityName city or locality name (nullable)
     * @param country  country name (nullable)
     * @return normalized location string, never null
     */
    public String formatLocation(String cityName, String country) {
        cityName = nonNull(cityName) ? cityName : "";
        country = nonNull(country) ? country : "";
        String location = "%s, %s".formatted(cityName, country);
        if (location.startsWith(", ")) {
            location = location.substring(2);
        }
        return location.trim();
    }

    /**
     * Derives a human-readable device label from a User-Agent string.
     * The label concatenates the detected device name and agent (browser/app) name,
     * with placeholder characters removed for readability.
     *
     * @param userAgent raw User-Agent header value
     * @return a concise device description, e.g., "iPhone Mobile Safari"
     */
    public String getDeviceDetails(String userAgent) {
        UserAgent client = this.parser.parse(userAgent);

        return "%s %s".formatted(client.getValue(UserAgent.DEVICE_NAME), client.getValue(UserAgent.AGENT_NAME)).replace("?", "");
    }

    /**
     * Verifies whether the current request originates from a known device for the user.
     * If unknown, triggers a notification and records the device; otherwise updates the last login timestamp.
     *
     * @param user    the authenticated user
     * @param request the HTTP request containing IP and User-Agent
     * @throws IOException      if GeoIP lookup fails due to I/O issues
     * @throws GeoIp2Exception  if the IP cannot be resolved by the GeoIP service
     */
    public void verifyDevice(Users user, HttpServletRequest request) throws IOException, GeoIp2Exception {

        String ip = AppUtil.extractIp(request);
        GeoIPDto location = getLocation(ip);

        String deviceDetails = getDeviceDetails(AppUtil.getUserAgent(request));

        DeviceMetadata existingDevice
                = findExistingDevice(user.getId(), deviceDetails, location.cityName());

        if (Objects.isNull(existingDevice)) {
            unknownDeviceNotification(deviceDetails, location,
                    ip, user.getEmail(), request.getLocale());

            DeviceMetadata deviceMetadata = DeviceMetadata.builder().userId(user.getId()).location(location.cityName()).deviceDetails(deviceDetails).lastLoggedIn(LocalDateTime.now()).build();
            customRepository.save(deviceMetadata);
        } else {
            existingDevice.setLastLoggedIn(LocalDateTime.now());
            customRepository.save(existingDevice);
        }
    }

    /**
     * Sends a notification to the user about a login from an unknown device/location.
     * Intended to inform the recipient and optionally verify the activity.
     *
     * @param deviceDetails human-readable device description
     * @param location      resolved geographic location of the IP
     * @param ip            source IP address of the request
     * @param email         recipient email address
     * @param locale        preferred locale for message formatting
     */
    private void unknownDeviceNotification(String deviceDetails, GeoIPDto location, String ip, @Size(max = 200, min = 5, message = "Not more than 200 and less than 5") @NotBlank(message = "recipient cannot be null") String email, Locale locale) {
    }

    /**
     * Looks up a known device for a user by matching device details and location.
     *
     * @param userId        the user identifier
     * @param deviceDetails normalized device description
     * @param location      normalized location (typically city)
     * @return the matching device metadata if found; otherwise null
     */
    private DeviceMetadata findExistingDevice(
            Long userId, String deviceDetails, String location) {
        List<DeviceMetadata> knownDevices
                = customRepository.findAll(DeviceMetadata.class, Map.of("user_id", userId), (rs, rn) -> {
                    DeviceMetadata deviceMetadata = new DeviceMetadata();
                    deviceMetadata.setId(rs.getLong("id"));
                    deviceMetadata.setUserId(rs.getLong("user_id"));
                    deviceMetadata.setDeviceDetails(rs.getString("device_details"));
                    deviceMetadata.setLocation(rs.getString("location"));
                    deviceMetadata.setLastLoggedIn(rs.getTimestamp("last_logged_in").toLocalDateTime());
                    return deviceMetadata;
                });

        for (DeviceMetadata existingDevice : knownDevices) {
            if (existingDevice.getDeviceDetails().equals(deviceDetails)
                    && existingDevice.getLocation().equals(location)) {
                return existingDevice;
            }
        }
        return null;
    }
}
