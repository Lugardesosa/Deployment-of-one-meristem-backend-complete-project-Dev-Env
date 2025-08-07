package org.meristem.oneapp.coreservice.domains.requests;


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
@Schema(description = "Request object for intellectual property asset")
public class IntellectualPropertyRequest extends AssetRequest {

    @Schema(description = "Type of intellectual property", example = "Trademark")
    @NotBlank(message = "Cannot be blank")
    private String propertyType;

    @Schema(description = "Registered name of the intellectual property", example = "Coca-Cola")
    @NotBlank(message = "Cannot be blank")
    @Size(max = 300, message = "Cannot be more than 300 chars")
    private String registeredName;

    @Schema(description = "Description of the intellectual property", example = "Trademark for beverage brand")
    @NotBlank(message = "Cannot be blank")
    @Size(max = 500, message = "Cannot be more than 500 chars")
    private String propertyDescription;
}
