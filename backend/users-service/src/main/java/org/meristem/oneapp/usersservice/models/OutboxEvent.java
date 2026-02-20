package org.meristem.oneapp.usersservice.models;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("outbox_event")
@Getter
@Setter
@NoArgsConstructor
public class OutboxEvent extends BaseModel<String> {

    private String aggregateType;
    private String eventKey;
    private Long aggregateId;
    private String eventType;
    private String eventClass;

    private String payload;

    private Integer outboxStatus;
    private int retryCount;

    private String lastError;

    private LocalDateTime sentAt;

    @Builder
    public OutboxEvent(Integer status, Long id, LocalDateTime createdDate, String createdBy, LocalDateTime lastModifiedDate, String lastModifiedBy, Integer version, String aggregateType, Long aggregateId, String eventType, String payload, Integer outboxStatus, int retryCount, String lastError, String eventKey, LocalDateTime sentAt, String eventClass) {
        super(id, createdDate, createdBy, lastModifiedDate, lastModifiedBy, version, status);
        this.aggregateType = aggregateType;
        this.aggregateId = aggregateId;
        this.eventType = eventType;
        this.payload = payload;
        this.outboxStatus = outboxStatus;
        this.retryCount = retryCount;
        this.lastError = lastError;
        this.eventKey = eventKey;
        this.sentAt = sentAt;
        this.eventClass = eventClass;
    }
}
