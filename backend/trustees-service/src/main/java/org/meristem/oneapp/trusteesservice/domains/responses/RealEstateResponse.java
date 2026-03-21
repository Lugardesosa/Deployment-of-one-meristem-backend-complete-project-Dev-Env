package org.meristem.oneapp.trusteesservice.domains.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;


@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
@Schema(description = "Response object for real estate asset")
public class RealEstateResponse extends AssetResponse {

    @Schema(description = "Type of property", example = "Residential")
    private String propertyType;

    @Schema(description = "Description of the property", example = "A 3-bedroom apartment in Lekki")
    private String propertyDescription;

    @Schema(description = "Address of the property", example = "12 Lekki Phase 1, Lagos")
    private String propertyAddress;

    @Schema(description = "Document details")
    private DocumentResponse documentResponse;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    @Schema(description = "File Response object for real estate asset")
    public static class DocumentResponse {

        @Schema(description = "The assetType of the asset", example = "1")
        private Long id;

        @Schema(example = "myimage.png", description = "The file")
        private String fileKey;

        @Schema(example = "image/png", description = "The file content type")
        private String contentType;

        @Schema(allowableValues = {"0", "1"}, example = "1", description = "Pass 0 if file is an image and 1 if file is a document")
        private Integer fileType;
    }
}
