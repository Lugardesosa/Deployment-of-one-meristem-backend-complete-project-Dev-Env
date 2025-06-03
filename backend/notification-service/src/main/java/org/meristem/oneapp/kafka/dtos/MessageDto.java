package org.meristem.oneapp.kafka.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.meristem.oneapp.notificationservice.domains.enums.MessageMedium;
import org.meristem.oneapp.notificationservice.domains.enums.MessageType;

@JsonIgnoreProperties(ignoreUnknown = true)
public record MessageDto(MessageMedium medium, MessageType type, MessageDetailsDto message) {
}
