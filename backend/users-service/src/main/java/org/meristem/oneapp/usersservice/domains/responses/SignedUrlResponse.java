package org.meristem.oneapp.usersservice.domains.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.io.Serializable;

@Builder
@Schema(description = "Response containing a signed URL")
public record SignedUrlResponse(
        @Schema(example = "https://example.com/signed-url", description = "The signed URL for accessing a resource")
        String signedUrl,
        @Schema(example = "myimage.png", description = "The file key")
        String fileKey,
        @Schema(example = "image/png", description = "The file content type")
        String contentType,
        @Schema(allowableValues = {"0", "1"}, example = "1", description = "Pass 0 if file is an image and 1 if file is a document")
        Integer fileType) implements Serializable {
}
