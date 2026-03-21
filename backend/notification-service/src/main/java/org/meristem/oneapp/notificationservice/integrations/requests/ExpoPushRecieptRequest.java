package org.meristem.oneapp.notificationservice.integrations.requests;

import lombok.Builder;

import java.util.List;

@Builder
public record ExpoPushRecieptRequest(List<String> ids) {
}
