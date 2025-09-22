package org.meristem.oneapp.kafka.dtos;

import lombok.Builder;
import org.jspecify.annotations.NonNull;
import org.meristem.oneapp.usersservice.domains.enums.MessageMedium;
import org.meristem.oneapp.usersservice.domains.enums.MessageType;

@Builder
public record MessageDto(@NonNull MessageMedium medium, @NonNull MessageType type, boolean isHtml, @NonNull Object message, @NonNull String classSimpleName) {
}
