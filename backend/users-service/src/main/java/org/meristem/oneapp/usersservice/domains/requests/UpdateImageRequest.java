package org.meristem.oneapp.usersservice.domains.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.meristem.oneapp.usersservice.domains.enums.ImageType;

public record UpdateImageRequest(@Schema(example = "myPicture.jpg", description = "pass the image") @NotBlank(message = "cannot be null") String imageKey,
                                 @Schema(anyOf = {ImageType.class}, example = "AVATAR", description = "pass the type of image being uploaded") @NotNull(message = "cannot be null") ImageType imageType,
                                 @Schema(example = "image/png", description = "Pass the content type of the image if it is PROFILE_PICTURE")
                                 String contentType) {
}
