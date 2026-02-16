package org.meristem.oneapp.usersservice.domains.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SmileIdWebhookNotification implements Serializable {

    @JsonProperty("Actions")
    private Map<String, String> actions;

    @JsonProperty("ConfidenceValue")
    private String confidenceValue;

    @JsonProperty("PartnerParams")
    private PartnerParams partnerParams;

    @JsonProperty("ImageLinks")
    private ImageLinks imageLinks;

    @JsonProperty("ResultCode")
    private String resultCode;

    @JsonProperty("ResultText")
    private String resultText;

    @JsonProperty("SmileJobID")
    private String smileJobId;

    @JsonProperty("Source")
    private String source;

    @JsonProperty("timestamp")
    private String timestamp;

    @JsonProperty("signature")
    private String signature;

    @JsonProperty("Country")
    private String country;

    @JsonProperty("DOB")
    private String dateOfBirth;

    @JsonProperty("ExpirationDate")
    private String expirationDate;

    @JsonProperty("FullName")
    private String fullName;

    @JsonProperty("IDNumber")
    private String idNumber;

    @JsonProperty("IDType")
    private String idType;

    @JsonProperty("Photo")
    private String photo;

    @JsonProperty("Document")
    private String document;

    @JsonProperty("Gender")
    private String gender;

    @JsonProperty("IssuanceDate")
    private String issuanceDate;

    @JsonProperty("KYCReceipt")
    private String kycReceipt;

    @JsonProperty("PhoneNumber2")
    private String phoneNumber2;

    @JsonProperty("SecondaryIDNumber")
    private String secondaryIdNumber;

    @JsonProperty("Address")
    private String address;

    @JsonProperty("CountryOfBirth")
    private String countryOfBirth;

    @JsonProperty("DateOfDeath")
    private String dateOfDeath;

    @JsonProperty("Email")
    private String email;

    @JsonProperty("FirstName")
    private String firstName;

    @JsonProperty("IDNumberPreviouslyRegistered")
    private boolean idNumberPreviouslyRegistered;

    @JsonProperty("IDStatus")
    private String idStatus;

    @JsonProperty("IsAlive")
    private boolean isAlive;

    @JsonProperty("LastName")
    private String lastName;

    @JsonProperty("LocalAreaOfOrigin")
    private String localAreaOfOrigin;

    @JsonProperty("Nationality")
    private String nationality;

    @JsonProperty("Occupation")
    private String occupation;

    @JsonProperty("OtherNames")
    private String middleName;

    @JsonProperty("PhoneNumber")
    private String phoneNumber;

    @JsonProperty("PlaceOfBirth")
    private String placeOfBirth;

    @JsonProperty("PlaceOfIssuance")
    private String placeOfIssuance;

    @JsonProperty("RegionOfOrigin")
    private String regionOfOrigin;

    @JsonProperty("Title")
    private String title;

    @JsonProperty("UserIDsOfPreviousRegistrants")
    private List<String> userIdsOfPreviousRegistrants;

    private String bvn;
    private String bvnHashed;
    private boolean emailVerified;
    private boolean passwordSet;

    public record PartnerParams(
            @JsonProperty("job_id")
            String jobId,

            @JsonProperty("job_type")
            int jobType,

            // user email
            @JsonProperty("user_id")
            String userId
    ) implements Serializable {
    }

    public record ImageLinks(
            @JsonProperty("id_card_back")
            String idCardBack,

            @JsonProperty("id_card_image")
            String idCardImage,

            @JsonProperty("selfie_image")
            String selfieImage
    ) implements Serializable {
    }

    public String getMiddleName() {
        return StringUtils.isBlank(this.middleName) ? null : this.middleName.trim();
    }

    public String getFirstName() {
        return this.firstName.trim();
    }

    public String getLastName() {
        return this.lastName.trim();
    }
}