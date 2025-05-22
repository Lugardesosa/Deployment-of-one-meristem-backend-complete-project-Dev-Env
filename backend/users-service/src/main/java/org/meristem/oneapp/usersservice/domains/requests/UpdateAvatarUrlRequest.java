package org.meristem.oneapp.usersservice.domains.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

public record UpdateAvatarUrlRequest(@Schema(example = "1", description = "pass the avatar's id") @NotNull(message = "cannot be null") Long avatarId) {
}
