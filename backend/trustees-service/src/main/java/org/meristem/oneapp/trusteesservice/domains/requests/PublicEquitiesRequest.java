package org.meristem.oneapp.trusteesservice.domains.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
@Schema(description = "Request object for public equities operations")
public class PublicEquitiesRequest extends EquitiesRequest {

        @NotBlank(message = "Cannot be blank")
        @Size(max = 50, message = "Cannot be more than 50 chars")
        @Schema(description = "CSCS number", example = "CSCS12345", requiredMode = Schema.RequiredMode.REQUIRED, maxLength = 50)
        private String cscsNumber;

        @NotBlank(message = "Cannot be blank")
        @Size(max = 50, message = "Cannot be more than 50 chars")
        @Schema(description = "CHN (Clearing House Number)", example = "CHN67890", requiredMode = Schema.RequiredMode.REQUIRED, maxLength = 50)
        private String chn;
}
