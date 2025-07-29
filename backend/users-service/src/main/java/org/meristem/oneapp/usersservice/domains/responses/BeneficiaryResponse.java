package org.meristem.oneapp.usersservice.domains.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.meristem.oneapp.usersservice.domains.enums.BeneficiaryRelationship;
import org.meristem.oneapp.usersservice.domains.enums.Gender;
import org.meristem.oneapp.usersservice.domains.enums.MaritalStatus;
import org.meristem.oneapp.usersservice.validations.constraints.AllPhoneNumber;
import org.meristem.oneapp.usersservice.validations.constraints.Email;

import java.time.LocalDate;

@Builder
public record BeneficiaryResponse(

        @Schema(description = "First name of the beneficiary", example = "John")
        String firstName,

        @Schema(description = "Last name of the beneficiary", example = "Doe")
        String lastName,

        @Schema(description = "Relationship of the beneficiary to the account owner", example = "SPOUSE")
        String beneficiaryRelationship,

        @Schema(description = "Gender of the beneficiary", example = "MALE")
        String gender,

        @Schema(description = "Email address of the beneficiary", example = "john.doe@example.com")
        String email,

        @Schema(description = "Phone number of the beneficiary", example = "08012345678")
        String phoneNumber,

        @Schema(description = "Date of birth of the beneficiary", example = "1990-05-15", type = "string", format = "date")
        LocalDate dob,

        @Schema(description = "Address of the beneficiary", example = "123 Lagos Street, Lekki")
        String address,

        @Schema(description = "Marital status of the beneficiary", example = "MARRIED")
        String maritalStatus,

        @Schema(description = "Bank name of the beneficiary", example = "Access Bank Plc")
        String bankName,

        @Schema(description = "Bank account number of the beneficiary", example = "0123456789")
        String accountNumber
) {
}
