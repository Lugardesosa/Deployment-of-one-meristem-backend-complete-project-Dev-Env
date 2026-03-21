package org.meristem.oneapp.usersservice.integrations.requests;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record SmileIdEnhancedKycRequest(
        String callback_url,
        String country,
        String dob,
        String first_name,
        String gender,
        @JsonProperty("id_number")
        String idNumber,
        @JsonProperty("id_type")
        String idType,
        String last_name,
        String middle_name,
        @JsonProperty("partner_id")
        String partnerId,
        @JsonProperty("partner_params")
        PartnerParams partnerParams,
        String phone_number,
        String signature,
        @JsonProperty("source_sdk")
        String sourceSdk,
        @JsonProperty("source_sdk_version")
        String sourceSdkVersion,
        String timestamp
) {

    public static SmileIdEnhancedKycRequest newRequest(String idNumber, String idType, String partnerId, PartnerParams partnerParams, String signature, String timestamp, String country) {
        return new SmileIdEnhancedKycRequest(null, country, null, null, null, idNumber, idType, null, null, partnerId,
                partnerParams, null, signature, "rest_api", null, timestamp);
    }

    @Builder
    public record PartnerParams(
            String job_id,
            int job_type,
            String user_id
    ) {}
}
