package org.meristem.oneapp.coreservices.notifications.dtos;

public record NotificationRequest<T> (T messageDetails) {
}
