package org.meristem.oneapp.coreservices.notifications.repositories;

import org.meristem.oneapp.coreservices.notifications.models.ExpoNotificationTicket;

import java.time.LocalDateTime;
import java.util.List;

public interface ExpoNotificationTicketRepository extends BaseRepository<ExpoNotificationTicket, Long> {
    List<ExpoNotificationTicket> findByCreatedDateBefore(LocalDateTime createdDateBefore);

    List<ExpoNotificationTicket> findTicketIdByCreatedDateBefore(LocalDateTime createdDateBefore);
}
