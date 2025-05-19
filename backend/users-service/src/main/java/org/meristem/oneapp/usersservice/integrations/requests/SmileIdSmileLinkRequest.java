package org.meristem.oneapp.usersservice.integrations.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.util.List;
import java.util.Map;

@Builder
public record SmileIdSmileLinkRequest(
        @JsonProperty("partner_id")
        String partnerId,

        String signature,
        String timestamp,
        String name,

        @JsonProperty("company_name")
        String companyName,

        @JsonProperty("id_types")
        List<IdType> idTypes,

        @JsonProperty("callback_url")
        String callbackUrl,

        @JsonProperty("data_privacy_policy_url")
        String dataPrivacyPolicyUrl,

        @JsonProperty("logo_url")
        String logoUrl,

        @JsonProperty("is_single_use")
        boolean isSingleUse,

        @JsonProperty("user_id")
        String userId,

        @JsonProperty("partner_params")
        Map<String, String> partnerParams,

        @JsonProperty("expires_at")
        String expiresAt
) {

    public record IdType(
            @Schema(name = "country", example = "NG", description = "Pass the country code") @NotBlank(message = "Cannot be null") String country,
            @Schema(name = "idType", example = "PASSPORT", description = "Pass the id type") @NotBlank(message = "Cannot be null") @JsonProperty("id_type")
            String idType,

            @Schema(name = "verificationMethod", example = "doc_verification", description = "Pass the verification method") @JsonProperty("verification_method")
            String verificationMethod
    ) {}
}
