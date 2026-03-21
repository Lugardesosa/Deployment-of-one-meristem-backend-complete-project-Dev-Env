package org.meristem.oneapp.usersservice.domains.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.meristem.oneapp.usersservice.domains.enums.Gender;
import org.springframework.data.relational.core.mapping.Column;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

@Schema(name = "UsersResponse", description = "User details, profile metadata, and access options returned by the Users service.")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UsersResponse implements Serializable {

    @ArraySchema(
            arraySchema = @Schema(description = "Financial instruments the user can access."),
            schema = @Schema(implementation = UserInstrumentResponse.class)
    )
    private List<UserInstrumentResponse> userInstrumentResponses = new ArrayList<>();

    @Schema(
            description = "User feature options grouped by category.",
            example = "{\"MER-STOCKS\":[{\"id\":1,\"name\":\"Dollar Fund\",\"accessed\":true}],\"TRUSTEES\":[{\"id\":2,\"name\":\"Comprehensive WIll\",\"accessed\":false}]}"
    )
    private Map<String, Set<UserOptionResponse>> userOptionResponses = new HashMap<>();

    @Schema(
            description = "User's details",
            exampleClasses = UsersDetails.class
    )
    private UsersDetails usersDetails;

    @Schema(
            description = "User's dependents",
            exampleClasses = DependentAccounts.class
    )
    private List<DependentAccounts> dependents;

    private List<JointAccountDetailsResponse> jointAccountDetailsResponse;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class UsersDetails implements Serializable {
        @Schema(description = "Middleware id of the user.", example = "kskskms92kl2jms")
        private String middlewareCustomerId;

        @Schema(description = "Status code for this user.", example = "1")
        private Integer status;

        @Schema(description = "Unique identifier of the user.", example = "123456789", format = "int64")
        private Long id;

        @Schema(description = "Email address of the user.", example = "jane.doe@example.com")
        private String email;

        @Schema(description = "First name of the user.", example = "Jane")
        private String firstName;

        @Schema(description = "Last name of the user.", example = "Doe")
        private String lastName;

        @Schema(description = "Middle name or initial of the user.", example = "A.", nullable = true)
        private String middleName;

        @JsonIgnore
        @Schema(hidden = true, description = "Sensitive. Not exposed in API.")
        private String password;

        @Schema(description = "E.164 formatted phone number.", example = "+234551234567")
        private String phoneNumber;

        @JsonIgnore
        @Schema(hidden = true, description = "Sensitive. Not exposed in API.")
        private Integer passwordAttempt;

        @Column("image_key")
        @Schema(description = "Storage URL of the user's profile image.", example = "www.huaweicloud.com/images/profiles/abc123.png", nullable = true)
        private String image;

        @Schema(description = "User gender.", allowableValues = {"MALE", "FEMALE", "OTHER"}, example = "FEMALE", oneOf = Gender.class)
        private String gender;

        @Column("date_of_birth")
        @Schema(description = "Date of birth.", type = "string", format = "date", example = "1990-05-12")
        private LocalDate dateOfBirth;

        @Column("referral_code")
        @Schema(description = "Referral code associated with the user's account.", example = "REF-1A2B3C")
        private String referralCode;

        @JsonIgnore
        @Column("biometric_enabled")
        @Schema(hidden = true, description = "Sensitive. Not exposed in API.")
        private Boolean biometricEnabled;

        @Schema(description = "Pin set.")
        private Boolean pinSet;

        @Schema(description = "If the user does not want interest.")
        private Boolean interestFreeInvestment;

        @Schema(description = "If the user has set interest free investment.")
        private Boolean interestFreeInvestmentSet;

        private String cscsNumber;

        private String chnNumber;

        private Boolean emailVerified;

        private Integer accountType;

        @Schema(description = "States whether user allows all data sharing across subsidiary.", example = "true")
        private Boolean dataSharing;
        @Schema(description = "States whether user allows all data sharing for marketing purposes.", example = "true")
        private Boolean marketingDataSharing;
        @Schema(description = "States whether user allows all data sharing for ai and analytics.", example = "true")
        private Boolean aiAndAnalyticsDataSharing;
    }

    @Schema(name = "UserInstrumentResponse", description = "Instrument access information for the user.")
    @Builder
    public record UserInstrumentResponse(
            @Schema(description = "Instrument identifier.", example = "1", format = "int64")
            Long id,
            @Schema(description = "Human-readable instrument name.", example = "Wealth")
            String name,
            @Schema(description = "Code or short identifier for the instrument.", example = "MER-WEALTH")
            String code,
            @Schema(description = "Whether user wants to share this subsidiary's data with other subsidiaries.", example = "true")
            Boolean dataSharingAllowed,
            @Schema(description = "Whether the user completed onboarding.", example = "true")
            Boolean kycCompleted
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

    @Schema(name = "DependentAccounts", description = "Returns all this user's dependents.")
    @Builder
    public record DependentAccounts(
            @Schema(description = "The userId of the minor.", example = "1", format = "int64")
            Long userId,
            @Schema(description = "The customer id of the minor", example = "001127")
            String customerId,

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

            @Schema(description = "E.164 formatted phone number.", example = "+234551234567")
            String phoneNumber,

            @Column("image_key")
            @Schema(description = "Storage URL of the user's profile image.", example = "www.huaweicloud.com/images/profiles/abc123.png", nullable = true)
            String image,

            @Schema(description = "User gender.", allowableValues = {"MALE", "FEMALE", "OTHER"}, example = "FEMALE", oneOf = Gender.class)
            String gender,

            @Column("date_of_birth")
            @Schema(description = "Date of birth.", type = "string", format = "date", example = "1990-05-12")
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
            LocalDate dateOfBirth,

            Integer accountType
            ) implements Serializable {
    }
}
