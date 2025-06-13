package org.meristem.oneapp.usersservice.domains.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.io.Serializable;

@Builder
@Schema(description = "Response containing a signed URL")
public record SignedUrlResponse(
        @Schema(example = "https://example.com/signed-url", description = "The signed URL for accessing a resource")
        String signedUrl,
        @Schema(example = "myimage.png", description = "The image")
        String imageKey,
        @Schema(example = "image/png", description = "The file content type")
        String contentType) implements Serializable {
}
