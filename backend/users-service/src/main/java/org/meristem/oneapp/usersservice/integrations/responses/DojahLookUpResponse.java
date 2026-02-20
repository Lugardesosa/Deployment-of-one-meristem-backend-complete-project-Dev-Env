package org.meristem.oneapp.usersservice.integrations.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public record DojahLookUpResponse(Entity entity) {

    public record Entity(
            String bvn,

            @JsonProperty("first_name")
            String firstName,

            @JsonProperty("last_name")
            String lastName,

            @JsonProperty("middle_name")
            String middleName,

            String gender,

            @JsonProperty("date_of_birth")
            @JsonFormat(pattern = "yyyy-MM-dd")
            LocalDate dateOfBirth,

            @JsonProperty("phone_number1")
            String phoneNumber1,

            String image,

            String email,

            @JsonProperty("enrollment_bank")
            String enrollmentBank,

            @JsonProperty("enrollment_branch")
            String enrollmentBranch,

            @JsonProperty("level_of_account")
            String levelOfAccount,

            @JsonProperty("lga_of_origin")
            String lgaOfOrigin,

            @JsonProperty("lga_of_residence")
            String lgaOfResidence,

            @JsonProperty("marital_status")
            String maritalStatus,

            @JsonProperty("name_on_card")
            String nameOnCard,

            String nationality,

            @JsonProperty("phone_number2")
            String phoneNumber2,

            @JsonProperty("registration_date")
            @JsonFormat(pattern = "yyyy-MM-dd")
            LocalDate registrationDate,

            @JsonProperty("residential_address")
            String residentialAddress,

            @JsonProperty("state_of_origin")
            String stateOfOrigin,

            @JsonProperty("state_of_residence")
            String stateOfResidence,

            String title,

            @JsonProperty("watch_listed")
            String watchListed
    ) {

    }
}
