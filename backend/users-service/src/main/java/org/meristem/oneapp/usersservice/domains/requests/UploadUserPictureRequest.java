package org.meristem.oneapp.usersservice.domains.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record UploadUserPictureRequest(@Schema(example = "https://picsum.photos/200/300", description = "pass the url of the user's picture") @NotNull(message = "cannot be null") String pictureUrl) {
}
