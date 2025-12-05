package org.meristem.oneapp.usersservice.domains.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.meristem.oneapp.usersservice.domains.enums.FileType;
import org.meristem.oneapp.usersservice.domains.enums.UtilityBillType;

public record AddressVerificationRequest(
        @Schema(example = "my_bio.pdf", description = "pass the document name of the address") @NotBlank(message = "cannot be null") String fileKey,
        @Schema(anyOf = FileType.class, example = "DOCUMENT", description = "pass the type of the document uploaded") @NotNull(message = "cannot be null") FileType fileType,
        @Schema(example = "application/pdf", description = "Pass the content type of the document.") @NotBlank(message = "Cannot be blank") String contentType,
        @Schema(example = "40 Adepoju Street, Ikate, Lagos", description = "Pass your house address.") @NotBlank(message = "Cannot be blank") String houseAddress,
        @Schema(example = "Lekki", description = "Pass the city you reside in.") @NotBlank(message = "Cannot be blank") String city,
        @Schema(example = "Lagos", description = "Pass the state you reside in.") @NotBlank(message = "Cannot be blank") String state,
        @Schema(example = "LUTH", description = "Pass the nearest landmark of your address.") String landMark,
        @Schema(example = "ELECTRICITY_BILL", description = "Pass the type of utility bill you want to use.") @NotNull(message = "Cannot be blank") UtilityBillType utilityBillType
) {
}
