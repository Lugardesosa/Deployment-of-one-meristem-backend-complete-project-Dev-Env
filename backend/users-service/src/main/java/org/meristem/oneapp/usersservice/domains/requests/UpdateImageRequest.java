package org.meristem.oneapp.usersservice.domains.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateImageRequest(@Schema(example = "myPicture.jpg", description = "pass the imageKey") @NotBlank(message = "cannot be null") String imageKey,
                                 @Schema(example = "true", description = "is an avatar or the user's picture") @NotNull(message = "cannot be null") Boolean isAvatar) {
}
