package org.meristem.oneapp.kafka.dtos;

import lombok.Builder;

@Builder
public record CreateJointCustomerDto(

        String accountName,
        String additionalInfo,

        String person1TitleCd,
        String person1FirstName,
        String person1LastName,
        String person1OtherNames,
        String person1MobilePhone,
        String person1EmailAddress,
        String person1MaritalStatusCd,
        String person1AddressStreet,
        String person1AddressCity,
        String person1AddressStateCd,
        String person1AddressCountryCd,
        String person1AddressZipCode,
        String person1BvnNumber,
        String person1EmployerName,
        String person1EmployerPhoneNo,
        String person1IdnDocName,
        String person1NationalityCd,
        String person1GenderCd,

        String person2TitleCd,
        String person2FirstName,
        String person2LastName,
        String person2OtherNames,
        String person2MobilePhone,
        String person2EmailAddress,
        String person2MaritalStatusCd,
        String person2AddressStreet,
        String person2AddressCity,
        String person2AddressStateCd,
        String person2AddressCountryCd,
        String person2AddressZipCode,
        String person2NationalityCd,
        String person2GenderCd,

        String accountId

) {}