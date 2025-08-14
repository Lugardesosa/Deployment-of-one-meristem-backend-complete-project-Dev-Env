package org.meristem.oneapp.coreservice.domains.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.meristem.oneapp.coreservice.domains.requests.EquitiesRequest;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
@Schema(description = "Response object for public equities operations")
public class PublicEquitiesResponse extends EquitiesResponse {

        @Schema(description = "Type of company", example = "Public")
        private String companyType;

        @Schema(description = "CSCS number", example = "1234567890")
        private String cscsNumber;

        @Schema(description = "CHN (Clearing House Number)", example = "CHN123456")
        private String chn;
}
