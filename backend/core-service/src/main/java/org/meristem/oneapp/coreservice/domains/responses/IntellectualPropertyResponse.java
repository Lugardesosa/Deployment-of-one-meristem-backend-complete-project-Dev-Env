package org.meristem.oneapp.coreservice.domains.responses;


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
public class IntellectualPropertyResponse extends AssetResponse {

    @Schema(description = "Type of intellectual property", example = "Trademark")
    private String propertyType;

    @Schema(description = "Registered name of the intellectual property", example = "Coca-Cola")
    private String registeredName;

    @Schema(description = "Description of the intellectual property", example = "Trademark for beverage brand")
    private String propertyDescription;
}
