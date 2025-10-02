package org.meristem.oneapp.trusteesservice.domains.requests;


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
@Schema(description = "Request object for real estate asset")
public class RealEstateRequest extends AssetRequest {

    @Schema(description = "Type of property", example = "Residential")
    @NotBlank(message = "Cannot be blank")
    private String propertyType;

    @Schema(description = "Description of the property", example = "A 3-bedroom apartment in Lekki, Lagos")
    @NotBlank(message = "Cannot be blank")
    @Size( max = 300, message = "Cannot be more than 300 chars")
    private String propertyDescription;

    @Schema(description = "Address of the property", example = "12 Lekki Phase 1, Lagos")
    @NotBlank(message = "Cannot be blank")
    @Size( max = 300, message = "Cannot be more than 300 chars")
    private String propertyAddress;

    private DocumentRequest documentRequest;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    @Schema(description = "File Request object for real estate asset")
    public static class DocumentRequest {

        @Schema(example = "myimage1234.png", description = "The file")
        @NotBlank(message = "Cannot be blank")
        private String fileKey;

        @Schema(example = "image/png", description = "The file content type")
        @NotBlank(message = "Cannot be blank")
        private String contentType;

        @Schema(allowableValues = {"0", "1"}, example = "1", description = "Pass 0 if file is an image and 1 if file is a document")
        @NotNull(message = "Cannot be null")
        private Integer fileType;
    }
}
