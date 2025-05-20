package org.meristem.oneapp.usersservice.domains.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public record SmileIdWebhookNotification(

        @JsonProperty("Actions")
        Map<String, String> actions,

        @JsonProperty("ConfidenceValue")
        String confidenceValue,

        @JsonProperty("PartnerParams")
        PartnerParams partnerParams,

        @JsonProperty("ImageLinks")
        ImageLinks imageLinks,

        @JsonProperty("ResultCode")
        String resultCode,

        @JsonProperty("ResultText")
        String resultText,

        @JsonProperty("SmileJobID")
        String smileJobId,

        @JsonProperty("Source")
        String source,

        @JsonProperty("timestamp")
        String timestamp,

        @JsonProperty("signature")
        String signature,

        @JsonProperty("Country")
        String country,

        @JsonProperty("DOB")
        String dob,

        @JsonProperty("ExpirationDate")
        String expirationDate,

        @JsonProperty("FullName")
        String fullName,

        @JsonProperty("IDNumber")
        String idNumber,

        @JsonProperty("IDType")
        String idType,

        @JsonProperty("Photo")
        String photo,

        @JsonProperty("Document")
        String document,

        @JsonProperty("Gender")
        String gender,

        @JsonProperty("IssuanceDate")
        String issuanceDate,

        @JsonProperty("KYCReceipt")
        String kycReceipt,

        @JsonProperty("PhoneNumber2")
        String phoneNumber2,

        @JsonProperty("SecondaryIDNumber")
        String secondaryIdNumber
) {

    public record PartnerParams(
            @JsonProperty("job_id")
            String jobId,

            @JsonProperty("job_type")
            int jobType,

            // user email
            @JsonProperty("user_id")
            String userId
    ) {}

    public record ImageLinks(
        @JsonProperty("id_card_back")
        String idCardBack,

        @JsonProperty("id_card_image")
        String idCardImage,

        @JsonProperty("selfie_image")
        String selfieImage
    ) {}
}