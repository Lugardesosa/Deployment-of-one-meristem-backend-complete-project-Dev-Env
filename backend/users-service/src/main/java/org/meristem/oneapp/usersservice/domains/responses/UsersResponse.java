package org.meristem.oneapp.usersservice.domains.responses;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import org.meristem.oneapp.usersservice.domains.enums.Gender;
import org.springframework.data.relational.core.mapping.Column;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Schema(name = "UsersResponse", description = "User details, profile metadata, and access options returned by the Users service.")
@Builder
public record UsersResponse(
        @Schema(description = "Status code for this user.", example = "1")
        Integer status,

        @Schema(description = "Unique identifier of the user.", example = "123456789", format = "int64")
        Long id,

        @Schema(description = "Email address of the user.", example = "jane.doe@example.com")
        String email,

        @Schema(description = "First name of the user.", example = "Jane")
        String firstName,

        @Schema(description = "Last name of the user.", example = "Doe")
        String lastName,

        @Schema(description = "Middle name or initial of the user.", example = "A.", nullable = true)
        String middleName,

        @JsonIgnore
        @Schema(hidden = true, description = "Sensitive. Not exposed in API.")
        String password,

        @Schema(description = "E.164 formatted phone number.", example = "+234551234567")
        String phoneNumber,

        @JsonIgnore
        @Schema(hidden = true, description = "Sensitive. Not exposed in API.")
        Integer passwordAttempt,

        @Column("image_key")
        @Schema(description = "Storage URL of the user's profile image.", example = "www.huaweicloud.com/images/profiles/abc123.png", nullable = true)
        String image,

        @JsonIgnore
        @Schema(hidden = true, description = "Sensitive. Not exposed in API.")
        String pin,

        @Schema(description = "User gender.", allowableValues = {"MALE", "FEMALE", "OTHER"}, example = "FEMALE", oneOf = Gender.class)
        String gender,

        @Column("date_of_birth")
        @Schema(description = "Date of birth.", type = "string", format = "date", example = "1990-05-12")
        LocalDate dateOfBirth,

        @Schema(description = "Whether the user has set a password.", example = "true")
        Boolean passwordSet,

        @Schema(description = "Whether the user's email has been verified.", example = "false")
        Boolean emailVerified,

        @Column("referral_code")
        @Schema(description = "Referral code associated with the user's account.", example = "REF-1A2B3C")
        String referralCode,

        @Column("onboarding_completed")
        @Schema(description = "Whether the user completed onboarding.", example = "true")
        Boolean onboardingCompleted,

        @ArraySchema(
                arraySchema = @Schema(description = "Financial instruments the user can access."),
                schema = @Schema(implementation = UserInstrumentResponse.class)
        )
        List<UserInstrumentResponse> userInstrumentResponses,

        @Schema(description = "States whether all data sharing across subsidiary.", example = "true")
        Boolean allDataShared,

        @Schema(
                description = "User feature options grouped by category.",
                example = "{\"MER-STOCKS\":[{\"id\":1,\"name\":\"Dollar Fund\",\"accessed\":true}],\"TRUSTEES\":[{\"id\":2,\"name\":\"Comprehensive WIll\",\"accessed\":false}]}"
        )
        Map<String, Set<UserOptionResponse>> userOptionResponses,

        @JsonIgnore
        @Column("biometric_enabled")
        @Schema(hidden = true, description = "Sensitive. Not exposed in API.")
        Boolean biometricEnabled,

        @Schema(description = "Pin set.")
        Boolean pinSet,

        @Schema(description = "If the user does not want interest.")
        Boolean interestFreeInvestment,

        @Schema(description = "If the user has set interest free investment.")
        Boolean interestFreeInvestmentSet
) implements Serializable {

    public UsersResponse(Integer status, Long id, String email, String firstName, String lastName, String middleName, String phoneNumber,
                         String image, String gender, LocalDate dateOfBirth, Boolean passwordSet, Boolean emailVerified, String referralCode, Boolean onboardingCompleted, List<UserInstrumentResponse> userInstrumentResponses, Boolean allDataShared, Map<String, Set<UserOptionResponse>> userOptionResponses, Boolean biometricEnabled, Boolean pinSet, Boolean interestFreeInvestment, Boolean interestFreeInvestmentSet) {
        this(status, id, email, firstName, lastName, middleName, null, phoneNumber, null, image, "", gender, dateOfBirth, passwordSet, emailVerified, referralCode, onboardingCompleted, userInstrumentResponses, allDataShared, userOptionResponses, biometricEnabled, pinSet, interestFreeInvestment, interestFreeInvestmentSet);
    }

    public UsersResponse(Integer status, Long id, String email, String firstName, String lastName, String middleName, String phoneNumber,
                         String image, String gender, LocalDate dateOfBirth, Boolean passwordSet, Boolean emailVerified, String referralCode, Boolean onboardingCompleted, Boolean biometricEnabled) {
        this(status, id, email, firstName, lastName, middleName, null, phoneNumber, null, image, "", gender, dateOfBirth, passwordSet, emailVerified, referralCode, onboardingCompleted, null, null, null, biometricEnabled, null, null, null);
    }

    public static UsersResponse newResponse(Integer status, Long id, String email, String firstName, String lastName, String middleName, String phoneNumber,
                                     String image, String gender, LocalDate dateOfBirth, Boolean passwordSet, Boolean emailVerified, String referralCode, Boolean onboardingCompleted, List<UserInstrumentResponse> userInstrumentResponses, Boolean allDataShared, Map<String, Set<UserOptionResponse>> userOptionResponses, Boolean biometricEnabled, Boolean pinSet, Boolean interestFreeInvestment, Boolean interestFreeInvestmentSet) {
        return new UsersResponse(status, id, email, firstName, lastName, middleName, phoneNumber, image, gender, dateOfBirth, passwordSet, emailVerified, referralCode, onboardingCompleted, userInstrumentResponses, allDataShared, userOptionResponses, biometricEnabled, pinSet, interestFreeInvestment, interestFreeInvestmentSet);
    }

    @Schema(name = "UserInstrumentResponse", description = "Instrument access information for the user.")
    @Builder
    public record UserInstrumentResponse(
            @Schema(description = "Instrument identifier.", example = "1", format = "int64")
            Long id,
            @Schema(description = "Human-readable instrument name.", example = "Wealth")
            String name,
            @Schema(description = "Whether the instrument has been accessed by the user.", example = "true")
            Boolean accessed,
            @Schema(description = "Code or short identifier for the instrument.", example = "MER-WEALTH")
            String code,
            @Schema(description = "Whether user wants to share this subsidiary's data with other subsidiaries.", example = "true")
            Boolean dataSharingAllowed
    ) implements Serializable {

    }

    @Schema(name = "UserOptionResponse", description = "Feature option and access status for the user.")
    @Builder
    public record UserOptionResponse(
            @Schema(description = "Option identifier.", example = "10", format = "int64")
            Long id,
            @Schema(description = "Option name.", example = "Money Market Fund")
            String name,
            @Schema(description = "Whether the option has been accessed by the user.", example = "false")
            Boolean accessed
    ) implements Serializable {

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            UserOptionResponse that = (UserOptionResponse) o;
            return Objects.equals(id(), that.id()) && Objects.equals(name(), that.name());
        }

        @Override
        public int hashCode() {
            return Objects.hash(id(), name());
        }
    }
}
