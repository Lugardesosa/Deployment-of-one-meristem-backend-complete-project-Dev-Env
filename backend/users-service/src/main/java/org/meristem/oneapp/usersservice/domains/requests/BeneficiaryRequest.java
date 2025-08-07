package org.meristem.oneapp.usersservice.domains.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import org.meristem.oneapp.usersservice.domains.enums.BeneficiaryRelationship;
import org.meristem.oneapp.usersservice.domains.enums.Gender;
import org.meristem.oneapp.usersservice.domains.enums.MaritalStatus;
import org.meristem.oneapp.usersservice.validations.constraints.AllPhoneNumber;
import org.meristem.oneapp.usersservice.validations.constraints.Email;

import java.time.LocalDate;

@Builder
@Schema(description = "Payload for creating a beneficiary")
public record BeneficiaryRequest(

        @NotBlank(message = "Cannot be empty")
        @Schema(description = "First name of the beneficiary", example = "John")
        @Size(max = 100, message = "Cannot be more than 100 chars")
        String firstName,

        @NotBlank(message = "Cannot be empty")
        @Schema(description = "Last name of the beneficiary", example = "Doe")
        @Size(max = 100, message = "Cannot be more than 100 chars")
        String lastName,

        @NotNull(message = "Cannot be null")
        @Schema(description = "Relationship of the beneficiary to the account owner", example = "SPOUSE")
        BeneficiaryRelationship beneficiaryRelationship,

        @NotNull(message = "Cannot be null")
        @Schema(description = "Gender of the beneficiary", example = "MALE")
        Gender gender,

        @NotBlank(message = "Cannot be empty")
        @Email(message = "Email must be valid")
        @Schema(description = "Email address of the beneficiary", example = "john.doe@example.com")
        @Size(max = 300, message = "Cannot be more than 300 chars")
        String email,

        @NotBlank(message = "Cannot be empty")
        @Schema(description = "Phone number of the beneficiary", example = "08012345678")
        @AllPhoneNumber(message = "Enter a valid phone number")
        String phoneNumber,

        @NotNull(message = "Cannot be null")
        @Schema(description = "Date of birth of the beneficiary", example = "1990-05-15", type = "string", format = "date")
        LocalDate dob,

        @NotBlank(message = "Cannot be empty")
        @Schema(description = "Address of the beneficiary", example = "123 Lagos Street, Lekki")
        @Size(max = 500, message = "Cannot be more than 500 chars")
        String address,

        @NotNull(message = "Cannot be null")
        @Schema(description = "Marital status of the beneficiary", example = "MARRIED")
        MaritalStatus maritalStatus,

        @NotBlank(message = "Cannot be empty")
        @Schema(description = "Bank name of the beneficiary", example = "Access Bank Plc")
        @Size(max = 100, message = "Cannot be more than 100 chars")
        String bankName,

        @NotBlank(message = "Cannot be empty")
        @Schema(description = "Bank account number of the beneficiary", example = "0123456789")
        @Size(max = 20, message = "Cannot be more than 20 chars")
        String accountNumber
) {
}