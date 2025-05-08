package org.meristem.oneapp.kafka.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import org.meristem.oneapp.usersservice.domains.enums.MessageMedium;
import org.meristem.oneapp.usersservice.domains.enums.MessageType;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public record MessageDto(MessageMedium medium, MessageType type, MessageDetailsDto message) {
}
