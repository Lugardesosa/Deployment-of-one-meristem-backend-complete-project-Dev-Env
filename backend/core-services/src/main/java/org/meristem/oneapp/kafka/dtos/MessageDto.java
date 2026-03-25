package org.meristem.oneapp.kafka.dtos;

import lombok.Builder;
import org.jspecify.annotations.NonNull;
import org.meristem.oneapp.coreservices.notifications.domains.enums.MessageMedium;
import org.meristem.oneapp.coreservices.notifications.domains.enums.MessageType;

@Builder
public record MessageDto(@NonNull MessageMedium medium, @NonNull MessageType type, boolean isHtml, Object message, String classSimpleName) {
}
