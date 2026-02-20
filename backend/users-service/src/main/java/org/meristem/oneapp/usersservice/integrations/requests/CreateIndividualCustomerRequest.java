package org.meristem.oneapp.usersservice.integrations.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record CreateIndividualCustomerRequest(
        String firstName,
        String lastName,
        String otherNames,
        String primaryEmailAddress,
        String mobilePhoneNo,
        String dateOfBirth,
        String nationalId,
        String employerName,
        String occupation,
        String genderCd,
        String addressStreet,
        String addressCity,
        String addressCountryCd
) {
}
