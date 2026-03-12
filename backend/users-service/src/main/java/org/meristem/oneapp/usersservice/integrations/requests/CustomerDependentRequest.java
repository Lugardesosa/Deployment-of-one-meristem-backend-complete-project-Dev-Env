package org.meristem.oneapp.usersservice.integrations.requests;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDependentRequest {

    private String titleCd;
    private String lastName;
    private String firstName;
    private String otherNames;
    private String genderCd;
    private String birthDate;
    private String motherMaidenName;
    private String parentCustomerId;
    private String nationalityCd;
    private String mobilePhoneNo;
    private String alternatePhoneNo;
    private String primaryEmailAddress;
    private String alternateEmailAddress;

    private String addressStreet;
    private String addressCity;
    private String addressStateCd;
    private String addressCountryCd;
    private String addressZipCode;

    private String bankCd;
    private String bankAccountName;
    private String bankAccountNo;
    private String bankBranchName;
    private String bankAddressDetails;

    private String identityDocTypeCd;
    private String identityDocName;
    private String identityDocNo;
    private String identityDocIssueDate;
    private String identityDocExpiryDate;
    private String identityDocIssueAuthority;

    private String locationCd;
    private String externalReference1;
    private String externalReference2;
    private String externalCrmId;
    private String officerId;
    private String introducerId;

    private String parentFirstName;
    private String parentLastName;
    private String parentTitleCd;
    private String parentGenderCd;
    private String parentMobilePhoneNo;
    private String parentEmailAddress;

    private String parentAddressStreet;
    private String parentAddressCity;
    private String parentAddressStateCd;
    private String parentAddressCountryCd;

    private String parentIdentityDocTypeCd;
    private String parentIdentityDocNo;
    private String parentRelationshipCd;
}
