package org.meristem.oneapp.usersservice.domains.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "Response containing the status and URL of the uploaded profile picture")
public record ProfilePictureUploadResponse(
        @Schema(example = "True", description = "The status of the upload operation")
        String status,
        @Schema(example = "https://example.com/profile-picture-url", description = "The URL of the uploaded profile picture")
        String url) {
}
