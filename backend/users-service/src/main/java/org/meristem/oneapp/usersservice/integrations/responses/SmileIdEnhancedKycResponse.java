package org.meristem.oneapp.usersservice.integrations.responses;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.Map;

@Builder
public record SmileIdEnhancedKycResponse(

        @JsonProperty("Actions")
        SmileIdEnhancedKycResponse.Actions actions,

        @JsonProperty("Address")
        String address,

        @JsonProperty("Country")
        String country,

        @JsonProperty("CountryOfBirth")
        String countryOfBirth,

        @JsonProperty("DOB")
        String dateOfBirth,

        @JsonProperty("DateOfDeath")
        String dateOfDeath,

        @JsonProperty("Document")
        String document,

        @JsonProperty("Email")
        String email,

        @JsonProperty("ExpirationDate")
        String expirationDate,

        @JsonProperty("FirstName")
        String firstName,

        @JsonProperty("FullData")
        Map<String, Object> fullData,

        @JsonProperty("FullName")
        String fullName,

        @JsonProperty("Gender")
        String gender,

        @JsonProperty("IDNumber")
        String idNumber,

        @JsonProperty("IDStatus")
        String idStatus,

        @JsonProperty("IDType")
        String idType,

        @JsonProperty("ImageLinks")
        SmileIdEnhancedKycResponse.ImageLinks imageLinks,

        @JsonProperty("IsAlive")
        String isAlive,

        @JsonProperty("IssuanceDate")
        String issuanceDate,

        @JsonProperty("LastName")
        String lastName,

        @JsonProperty("LocalAreaOfOrigin")
        String localAreaOfOrigin,

        @JsonProperty("Nationality")
        String nationality,

        @JsonProperty("Occupation")
        String occupation,

        @JsonProperty("OtherNames")
        String otherNames,

        @JsonProperty("PartnerParams")
        SmileIdEnhancedKycResponse.PartnerParams partnerParams,

        @JsonProperty("PhoneNumber")
        String phoneNumber,

        @JsonProperty("PhoneNumber2")
        String phoneNumber2,

        @JsonProperty("Photo")
        String photo,

        @JsonProperty("PlaceOfBirth")
        String placeOfBirth,

        @JsonProperty("PlaceOfIssuance")
        String placeOfIssuance,

        @JsonProperty("RegionOfOrigin")
        String regionOfOrigin,

        @JsonProperty("ResultCode")
        String resultCode,

        @JsonProperty("ResultText")
        String resultText,

        @JsonProperty("SecondaryIDNumber")
        String secondaryIdNumber,

        @JsonProperty("SmileJobID")
        String smileJobId,

        @JsonProperty("Source")
        String source,

        @JsonProperty("Title")
        String title,

        @JsonProperty("signature")
        String signature,

        @JsonProperty("timestamp")
        String timestamp
) {
    public record Actions(
            @JsonProperty("Return_Personal_Info")
            String returnPersonalInfo,

            @JsonProperty("Verify_ID_Number")
            String verifyIdNumber
    ) {}

    public record ImageLinks(
            @JsonProperty("id_photo_image")
            String idPhotoImage
    ) {}

    public record PartnerParams(
            @JsonProperty("job_id")
            String jobId,

            @JsonProperty("job_type")
            int jobType,

            @JsonProperty("user_id")
            String userId
    ) {}
}
