package org.meristem.oneapp.coreservice.domains.requests;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
@Schema(description = "Request object for public equities operations")
public class EquitiesRequest extends AssetRequest {

        @NotBlank(message = "Cannot be blank")
        @Size(max = 50, message = "Cannot be more than 50 chars")
        @Schema(description = "Type of the company", example = "Private", requiredMode = Schema.RequiredMode.REQUIRED, maxLength = 50)
        private String companyType;

        @NotBlank(message = "Cannot be blank")
        @Size(max = 150, message = "Cannot be more than 150 chars")
        @Schema(description = "Name of the brokerage house", example = "XYZ Securities", requiredMode = Schema.RequiredMode.REQUIRED, maxLength = 150)
        private String brokerageHouse;

        @NotBlank(message = "Cannot be blank")
        @Size(max = 150, message = "Cannot be more than 150 chars")
        @Schema(description = "Name of the share", example = "ABC Corp", requiredMode = Schema.RequiredMode.REQUIRED, maxLength = 150)
        private String shareName;

        @NotNull(message = "Cannot be blank")
        @Schema(description = "Number of units", example = "100", requiredMode = Schema.RequiredMode.REQUIRED)
        private Integer noOfUnits;

}
