package org.meristem.oneapp.usersservice.services;

import com.maxmind.geoip2.exception.GeoIp2Exception;
import jakarta.servlet.http.HttpServletRequest;
import org.meristem.oneapp.kafka.dtos.GeoIPDto;
import org.meristem.oneapp.usersservice.models.DeviceMetadata;
import org.meristem.oneapp.usersservice.models.Users;

import java.io.IOException;

/**
 * Interface for managing login-related operations.
 * Provides functionality for device tracking, geolocation, and user-agent parsing.
 */
public interface ILoginService {

    /**
     * Initializes the GeoIP database reader after bean construction.
     *
     * @throws IOException if the database file cannot be loaded
     */
    void init() throws IOException;

    /**
     * Resolves geographic information for the provided IPv4/IPv6 address.
     *
     * @param ip textual IP address to look up
     * @return a DTO containing city, country, latitude and longitude for the IP
     * @throws IOException      if the underlying database cannot be accessed
     * @throws GeoIp2Exception  if the IP cannot be resolved in the GeoIP database
     */
    GeoIPDto getLocation(String ip) throws IOException, GeoIp2Exception;

    /**
     * Persists the given device metadata record.
     *
     * @param deviceMetadata device information to store
     */
    void saveDevice(DeviceMetadata deviceMetadata);

    /**
     * Builds a displayable "city, country" string from the given parts.
     *
     * @param cityName city or locality name (nullable)
     * @param country  country name (nullable)
     * @return normalized location string, never null
     */
    String formatLocation(String cityName, String country);

    /**
     * Derives a human-readable device label from a User-Agent string.
     *
     * @param userAgent raw User-Agent header value
     * @return a concise device description
     */
    String getDeviceDetails(String userAgent);

    /**
     * Verifies whether the current request originates from a known device for the user.
     *
     * @param user    the authenticated user
     * @param request the HTTP request containing IP and User-Agent
     * @throws IOException      if GeoIP lookup fails due to I/O issues
     * @throws GeoIp2Exception  if the IP cannot be resolved by the GeoIP service
     */
    void verifyDevice(Users user, HttpServletRequest request) throws IOException, GeoIp2Exception;
}
