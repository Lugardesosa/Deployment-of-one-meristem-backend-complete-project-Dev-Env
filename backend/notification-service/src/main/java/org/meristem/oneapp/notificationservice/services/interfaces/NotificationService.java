package org.meristem.oneapp.notificationservice.services.interfaces;

public interface NotificationService<T> {

    void send(T request);
}
