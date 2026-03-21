package org.meristem.oneapp.usersservice.integrations.responses;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public record DojahNinLookUpResponse(Entity entity) {

    public record Entity(

            String nin,

            @JsonProperty("first_name")
            String firstName,

            @JsonProperty("last_name")
            String lastName,

            @JsonProperty("middle_name")
            String middleName,

            @JsonProperty("date_of_birth")
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
            LocalDate dateOfBirth,

            @JsonProperty("phone_number")
            String phoneNumber,

            // Base64
            String photo,

            String gender,

            String email,

            @JsonProperty("employment_status")
            String employmentStatus,

            @JsonProperty("marital_status")
            String maritalStatus,

            @JsonProperty("birth_country")
            String birthCountry,

            @JsonProperty("birth_lga")
            String birthLga,

            @JsonProperty("birth_state")
            String birthState,

            @JsonProperty("educational_level")
            String educationalLevel,

            @JsonProperty("maiden_name")
            String maidenName,

            @JsonProperty("nspoken_lang")
            String nSpokenLang,

            String profession,

            String religion,

            @JsonProperty("residence_address_line_1")
            String residenceAddressLine1,

            @JsonProperty("residence_address_line_2")
            String residenceAddressLine2,

            @JsonProperty("residence_status")
            String residenceStatus,

            @JsonProperty("ospoken_lang")
            String oSpokenLang,

            String height,

            @JsonProperty("p_first_name")
            String pFirstName,

            @JsonProperty("p_middle_name")
            String pMiddleName,

            @JsonProperty("p_last_name")
            String pLastName,

            @JsonProperty("tax_id")
            String taxId,

            @JsonProperty("tax_residency")
            String taxResidency,

            @JsonAlias({"origin_lga", "LocalAreaOfOrigin"})
            String originLga,
            @JsonProperty("origin_place")
            String originPlace,
            @JsonProperty("origin_state")
            String originState,

            @JsonProperty("residence_lga")
            String residenceLga,
            @JsonProperty("residence_town")
            String residenceTown,
            @JsonProperty("residence_state")
            String residenceState,
            @JsonProperty("residence_address_line_1")
            String residenceAddress
    ) {
    }
}
