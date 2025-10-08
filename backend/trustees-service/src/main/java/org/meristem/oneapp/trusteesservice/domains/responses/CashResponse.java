package org.meristem.oneapp.trusteesservice.domains.responses;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Response object for cash-related operations")
public class CashResponse extends AssetResponse {

        @Schema(description = "Name of the account", example = "John Doe", requiredMode = Schema.RequiredMode.REQUIRED)
        String accountName;

        @Schema(description = "Number of the account", example = "1234567890", requiredMode = Schema.RequiredMode.REQUIRED)
        String accountNumber;

        @Schema(description = "Type of the account", example = "Savings", requiredMode = Schema.RequiredMode.REQUIRED)
        String accountType;
}
