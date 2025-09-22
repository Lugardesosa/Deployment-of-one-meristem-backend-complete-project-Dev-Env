package org.meristem.oneapp.usersservice.integrations.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import org.meristem.oneapp.usersservice.domains.requests.IdTypesRequest;

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
        List<IdTypesRequest> idTypes,

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
}
