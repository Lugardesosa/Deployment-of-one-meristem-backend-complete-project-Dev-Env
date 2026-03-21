package org.meristem.oneapp.usersservice.integrations.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CreateJointCustomerRequest {
        private String accountName;
        private String additionalInfo;
        private String person1TitleCd;
        private String person1FirstName;
        private String person1LastName;
        private String person1OtherNames;
        private String person1GenderCd;
        private String person1MobilePhone;
        private String person1EmailAddress;
        private String person1MaritalStatusCd;
        private String person1AddressStreet;
        private String person1AddressCity;
        private String person1AddressStateCd;
        private String person1AddressCountryCd;
        private String person1AddressZipCode;
        private String person1BirthDate;
        private String person1BvnNumber;
        private String person1EmployerAddressCity;
        private String person1EmployerAddressCountryCd;
        private String person1EmployerAddressStateCd;
        private String person1EmployerAddressStreet;
        private String person1EmployerName;
        private String person1EmployerPhoneNo;
        private String person1IdnDocExpiryDate;
        private String person1IdnDocIssueAuth;
        private String person1IdnDocIssueDate;
        private String person1IdnDocName;
        private String person1IdnDocNo;
        private String person1IdnDocTypeCd;
        private String person1IdnDocYn;
        private String person1NationalityCd;
        private String person1OccupationCd;
        private String person2TitleCd;
        private String person2FirstName;
        private String person2LastName;
        private String person2OtherNames;
        private String person2GenderCd;
        private String person2MobilePhone;
        private String person2EmailAddress;
        private String person2MaritalStatusCd;
        private String person2AddressStreet;
        private String person2AddressCity;
        private String person2AddressStateCd;
        private String person2AddressCountryCd;
        private String person2AddressZipCode;
        private String person2BirthDate;
        private String person2BvnNumber;
        private String person2EmployerAddressCity;
        private String person2EmployerAddressCountryCd;
        private String person2EmployerAddressStateCd;
        private String person2EmployerAddressStreet;
        private String person2EmployerName;
        private String person2EmployerPhoneNo;
        private String person2IdnDocExpiryDate;
        private String person2IdnDocIssueAuth;
        private String person2IdnDocIssueDate;
        private String person2IdnDocName;
        private String person2IdnDocNo;
        private String person2IdnDocTypeCd;
        private String person2IdnDocYn;
        private String person2NationalityCd;
        private String person2OccupationCd;
        private String externalReference1;
        private String externalReference2;
        private String externalCrmId;
        private String officerId;
        private String introducerId;
}
