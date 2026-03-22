package org.meristem.oneapp.coreservices.notifications.models;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
@Table("expo_notification_ticket")
public class ExpoNotificationTicket extends BaseModel<String> {

    private String ticketId;

    @Builder
    public ExpoNotificationTicket(Long id, LocalDateTime createdDate, String createdBy, LocalDateTime lastModifiedDate, String lastModifiedBy, Integer version, String ticketId) {
        super(id, createdDate, createdBy, lastModifiedDate, lastModifiedBy, version);
        this.ticketId = ticketId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ExpoNotificationTicket that = (ExpoNotificationTicket) o;
        return Objects.equals(getTicketId(), that.getTicketId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTicketId());
    }
}
