package org.meristem.oneapp.usersservice.domains.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

public record UpdateAvatarUrlRequest(@Schema(example = "http://placeimg.com/640/480", description = "pass the avatar's url") @NotBlank(message = "cannot be null") @Size(max = 500, message = "Cannot be more than 500 chars") String avatarUrl) {
}
