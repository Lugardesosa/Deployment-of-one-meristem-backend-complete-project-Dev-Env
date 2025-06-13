package org.meristem.oneapp.usersservice.domains.requests;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record OkHiWebhookRequest(
        @JsonProperty("event_type") String eventType,
        @JsonProperty("event_id") String eventId,
        @JsonProperty("api_version") String apiVersion,
        @JsonProperty("event_timestamp") long eventTimestamp,
        @JsonProperty("data") Data data
) {
    public record Data(
            @JsonProperty("usage_types") List<String> usageTypes,
            @JsonProperty("user") User user,
            @JsonProperty("metadata") Metadata metadata,
            @JsonProperty("location") Location location,
            @JsonProperty("address_verification") AddressVerification addressVerification
    ) {}

    public record User(
            @JsonProperty("id") String id,
            @JsonProperty("first_name") String firstName,
            @JsonProperty("last_name") String lastName,
            @JsonProperty("phone") String phone,
            @JsonProperty("email") String email
    ) {}

    public record Metadata(
            @JsonProperty("org_id") String orgId,
            @JsonProperty("app_id") String appId,
            @JsonProperty("branch_id") String branchId,
            @JsonProperty("user_id") String userId,
            @JsonProperty("app_user_id") String appUserId
    ) {}

    public record Location(
            @JsonProperty("ward") String ward,
            @JsonProperty("formatted_address") String formattedAddress,
            @JsonProperty("post_code") String postCode,
            @JsonProperty("street_name") String streetName,
            @JsonProperty("property_number") String propertyNumber,
            @JsonProperty("neighborhood") String neighborhood,
            @JsonProperty("district") String district,
            @JsonProperty("lga") String lga,
            @JsonProperty("city") String city,
            @JsonProperty("state") String state,
            @JsonProperty("country") String country,
            @JsonProperty("country_code") String countryCode,
            @JsonProperty("geo_point") GeoPoint geoPoint,
            @JsonProperty("user_id") String userId,
            @JsonProperty("directions") String directions,
            @JsonProperty("id") String id,
            @JsonProperty("url") String url,
            @JsonProperty("plus_code") String plusCode,
            @JsonProperty("address_line_1") String addressLine1,
            @JsonProperty("usage_types") List<String> usageTypes
    ) {}

    public record GeoPoint(
            @JsonProperty("lat") double lat,
            @JsonProperty("lon") double lon
    ) {}

    public record AddressVerification(
            @JsonProperty("id") String id,
            @JsonProperty("status") String status,
            @JsonProperty("status_description") String statusDescription,
            @JsonProperty("verification_method") String verificationMethod,
            @JsonProperty("verification_provider") String verificationProvider,
            @JsonProperty("mode") String mode,
            @JsonProperty("location_id") String locationId
    ) {}
}
