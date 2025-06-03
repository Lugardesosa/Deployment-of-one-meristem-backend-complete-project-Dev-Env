package org.meristem.oneapp.usersservice.domains.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "Response containing a signed URL")
public record SignedUrlResponse(
        @Schema(example = "https://example.com/signed-url", description = "The signed URL for accessing a resource")
        String signedUrl,
        @Schema(example = "https://example.com/upload-url", description = "The URL where the file is uploaded to")
        String uploadUrl) {
}
