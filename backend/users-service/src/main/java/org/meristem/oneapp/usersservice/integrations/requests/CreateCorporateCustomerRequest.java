package org.meristem.oneapp.usersservice.integrations.requests;

import lombok.Builder;

@Builder
public record CreateCorporateCustomerRequest(
        String corporateTypeDm,
        String organizationName,
        String registrationNumber,
        String registrationDate,
        String registrationCountryCd,
        String taxIdentificationNumber,
        String businessSectorCd,
        String locationCd,
        String addressStreet,
        String addressCity,
        String addressStateCd,
        String addressCountryCd,
        String addressZipCode,
        String primaryEmailAddress,
        String alternateEmailAddress,
        String mobilePhoneNo,
        String alternatePhoneNo,
        String contactPersonFirstName,
        String contactPersonLastName,
        String contactPersonTitleCd,
        String contactPersonGenderCd,
        String contactPersonMobilePhoneNo,
        String contactPersonEmailAddress,
        String annualTurnover,
        String operatingLicense,
        String licenseExpiryDate,
        String tradeLicense,
        String operatingYears,
        String numberOfEmployees,
        String externalReference1,
        String externalReference2,
        String externalCrmId,
        String officerId,
        String introducerId
) {
}
