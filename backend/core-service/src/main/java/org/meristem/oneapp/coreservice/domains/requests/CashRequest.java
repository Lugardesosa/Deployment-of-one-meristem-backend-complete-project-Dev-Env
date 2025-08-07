package org.meristem.oneapp.coreservice.domains.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
@Schema(description = "Request object for cash-related operations")
public class CashRequest extends AssetRequest {

        @NotBlank(message = "Account name cannot be blank")
        @Schema(description = "Name of the account", example = "John Doe", requiredMode = Schema.RequiredMode.REQUIRED)
        private String accountName;

        @NotBlank(message = "Account number cannot be blank")
        @Schema(description = "Number of the account", example = "1234567890", requiredMode = Schema.RequiredMode.REQUIRED)
        private String accountNumber;

        @NotBlank(message = "Account type cannot be blank")
        @Schema(description = "Type of the account", example = "Savings", requiredMode = Schema.RequiredMode.REQUIRED)
        private String accountType;
}
