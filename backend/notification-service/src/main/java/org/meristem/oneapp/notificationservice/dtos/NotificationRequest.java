package org.meristem.oneapp.notificationservice.dtos;

public record NotificationRequest<T> (T messageDetails) {
}
