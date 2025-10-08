package org.meristem.oneapp.usersservice.domains.requests;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record IdTypesRequest(@Schema(name = "country", example = "NG", description = "Pass the country code") @NotBlank(message = "Cannot be null") String country,
                             @Schema(name = "idType", example = "PASSPORT", description = "Pass the id type") @NotBlank(message = "Cannot be blank")
                             @JsonProperty("id_type") @JsonAlias({"idType", "id_type"})
                             String idType,
                             @Schema(name = "verificationMethod", example = "doc_verification", description = "Pass the verification method")
                             @JsonProperty("verification_method") @JsonAlias({"verificationMethod", "verification_method"})
                             @NotBlank(message = "Cannot be blank") String verificationMethod) {
}
