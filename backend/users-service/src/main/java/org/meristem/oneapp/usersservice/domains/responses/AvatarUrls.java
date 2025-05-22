package org.meristem.oneapp.usersservice.domains.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record AvatarUrls(@Schema(example = "1", description = "The id of the avatar") Long id, @Schema(example = "http://placeimg.com/640/480", description = "Avatar Url") String url) {
}
