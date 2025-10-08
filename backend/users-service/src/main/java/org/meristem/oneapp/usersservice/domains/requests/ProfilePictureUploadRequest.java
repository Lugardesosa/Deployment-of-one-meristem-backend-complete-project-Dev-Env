package org.meristem.oneapp.usersservice.domains.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Response containing the URL of the uploaded profile picture")
public record ProfilePictureUploadRequest(
        @Schema(example = "https://example.com/uploaded-picture-url", description = "The URL of the uploaded profile picture")
        @NotBlank(message = "Cannot be blank") String pictureUrl) {
}
