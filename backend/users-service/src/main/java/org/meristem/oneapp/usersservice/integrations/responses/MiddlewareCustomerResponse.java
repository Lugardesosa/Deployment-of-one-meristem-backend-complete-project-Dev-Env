package org.meristem.oneapp.usersservice.integrations.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public record MiddlewareCustomerResponse(

        List<CustomerData> data
) {
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CustomerData implements Serializable {


        @JsonProperty("customer_id")
        private String customerId;

        @JsonProperty("created_on")
        private LocalDateTime createdOn;

        @JsonProperty("modified_on")
        private LocalDateTime modifiedOn;

        @JsonProperty("modified_yesno")
        private String modifiedYesNo;

        @JsonProperty("customer_type")
        private String customerType;

        @JsonProperty("parent_customer_name")
        private String parentCustomerName;

        @JsonProperty("parent_customer_id")
        private String parentCustomerId;

        @JsonProperty("is_minor_yesno")
        private String isMinorYesNo;

        @JsonProperty("is_staff_yesno")
        private String isStaffYesNo;

        @JsonProperty("kyc_completed")
        private String kycCompleted;

        @JsonProperty("kyc_complete_on")
        private LocalDateTime kycCompleteOn;

        @JsonProperty("is_blacklisted_yesno")
        private String isBlacklistedYesNo;

        @JsonProperty("blacklisted_on")
        private LocalDateTime blacklistedOn;

        @JsonProperty("introduced_by")
        private String introducedBy;

        @JsonProperty("care_officer")
        private String careOfficer;

        @JsonProperty("care_team")
        private String careTeam;

        @JsonProperty("location")
        private String location;

        @JsonProperty("customer_name")
        private String customerName;

        @JsonProperty("first_name")
        private String firstName;

        @JsonProperty("other_name")
        private String otherName;

        @JsonProperty("last_name")
        private String lastName;

        @JsonProperty("birth_date")
        private LocalDateTime birthDate;

        @JsonProperty("current_age")
        private Integer currentAge;

        @JsonProperty("wedding_anniversary")
        private LocalDateTime weddingAnniversary;

        @JsonProperty("nationality")
        private String nationality;

        @JsonProperty("customer_address")
        private String customerAddress;

        @JsonProperty("customer_city")
        private String customerCity;

        @JsonProperty("customer_state")
        private String customerState;

        @JsonProperty("customer_country")
        private String customerCountry;

        @JsonProperty("email_address")
        private String emailAddress;

        @JsonProperty("phone_numbers")
        private String phoneNumbers;

        @JsonProperty("bank_bvn")
        private String bankBvn;

        @JsonProperty("birth_location")
        private String birthLocation;

        @JsonProperty("external_reference")
        private String externalReference;

        @JsonProperty("identity_document_type")
        private String identityDocumentType;

        @JsonProperty("identity_document_name")
        private String identityDocumentName;

        @JsonProperty("identity_document_no")
        private String identityDocumentNo;

        @JsonProperty("customer_job_title")
        private String customerJobTitle;

        @JsonProperty("marital_status")
        private String maritalStatus;

        @JsonProperty("phone_no")
        private String phoneNo;

        @JsonProperty("gender_code")
        private String genderCode;

        @JsonProperty("gender_description")
        private String genderDescription;

        @JsonProperty("kyc_status")
        private String kycStatus;

        @JsonProperty("nin")
        private String nin;

        @JsonProperty("is_primary_customer_yesno")
        private String isPrimaryCustomerYesNo;

        @JsonProperty("login_id")
        private String loginId;

        @JsonProperty("care_officer_id")
        private String careOfficerId;

        @JsonProperty("introducer_id")
        private String introducerId;

        @Builder.Default
        private Boolean emailVerified = false;

        private boolean phoneNumberVerified;

        private boolean bvnFacialVerified;
    }
}
