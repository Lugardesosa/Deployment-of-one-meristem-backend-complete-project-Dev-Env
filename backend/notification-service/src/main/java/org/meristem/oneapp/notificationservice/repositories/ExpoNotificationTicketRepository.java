package org.meristem.oneapp.notificationservice.repositories;

import org.meristem.oneapp.notificationservice.models.ExpoNotificationTicket;

import java.time.LocalDateTime;
import java.util.List;

public interface ExpoNotificationTicketRepository extends BaseRepository<ExpoNotificationTicket, Long> {
    List<ExpoNotificationTicket> findByCreatedDateBefore(LocalDateTime createdDateBefore);

    List<ExpoNotificationTicket> findTicketIdByCreatedDateBefore(LocalDateTime createdDateBefore);
}
