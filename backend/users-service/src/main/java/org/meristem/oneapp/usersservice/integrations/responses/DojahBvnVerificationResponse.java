package org.meristem.oneapp.usersservice.integrations.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record DojahBvnVerificationResponse(Entity entity) {

    public record Entity(

            String bvn,

            @JsonProperty("first_name")
            String firstName,

            @JsonProperty("middle_name")
            String middleName,

            @JsonProperty("last_name")
            String lastName,

            @JsonProperty("date_of_birth")
            String dateOfBirth,

            @JsonProperty("phone_number1")
            String phoneNumber1,

            @JsonProperty("phone_number2")
            String phoneNumber2,

            String gender,

            String image,

            @JsonProperty("selfie_verification")
            SelfieVerification selfieVerification,

            @JsonProperty("selfie_image_url")
            String selfieImageUrl
    ) {}

    public record SelfieVerification(

            @JsonProperty("confidence_value")
            BigDecimal confidenceValue,

            boolean match
    ) {}
}