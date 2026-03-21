package org.meristem.oneapp.usersservice.integrations.responses;

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

        private String customerId;
        private LocalDateTime createdOn;
        private LocalDateTime modifiedOn;
        private String modifiedYesNo;
        private String customerType;
        private String parentCustomerName;
        private String parentCustomerId;
        private String isMinorYesNo;
        private String isStaffYesNo;
        private String kycCompleted;
        private LocalDateTime kycCompleteOn;
        private String isBlacklistedYesNo;
        private LocalDateTime blacklistedOn;
        private String introducedBy;
        private String careOfficer;
        private String careTeam;
        private String location;
        private String customerName;
        private String firstName;
        private String otherName;
        private String lastName;
        private LocalDateTime birthDate;
        private Integer currentAge;
        private LocalDateTime weddingAnniversary;
        private String nationality;
        private String customerAddress;
        private String customerCity;
        private String customerState;
        private String customerCountry;
        private String emailAddress;
        private String phoneNumbers;
        private String bankBvn;
        private String birthLocation;
        private String externalReference;
        private String identityDocumentType;
        private String identityDocumentName;
        private String identityDocumentNo;
        private String customerJobTitle;
        private String maritalStatus;
        private String phoneNo;
        private String genderCode;
        private String genderDescription;
        private String kycStatus;
        private String nin;
        private String isPrimaryCustomerYesNo;
        private String loginId;
        private String careOfficerId;
        private String introducerId;
        @Builder.Default
        private Boolean emailVerified = false;
    }
}
