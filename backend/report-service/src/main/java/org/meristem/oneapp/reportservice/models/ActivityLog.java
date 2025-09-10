package org.meristem.oneapp.reportservice.models;

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
@Table("activity_log")
public class ActivityLog extends BaseModel<String> {

    private LocalDateTime activityDate;
    private String actor;
    private String action;

    // class simple name
    private String entity;
    private Long entityId;
    private String application;
    private String activity;

    @Builder
    public ActivityLog(Long id, LocalDateTime createdDate, String createdBy, LocalDateTime lastModifiedDate, String lastModifiedBy, Integer version, LocalDateTime activityDate, String actor, String action, String entity, Long entityId, String application, String activity) {
        super(id, createdDate, createdBy, lastModifiedDate, lastModifiedBy, version);
        this.activityDate = activityDate;
        this.actor = actor;
        this.action = action;
        this.entity = entity;
        this.entityId = entityId;
        this.application = application;
        this.activity = activity;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ActivityLog that = (ActivityLog) o;
        return Objects.equals(getEntity(), that.getEntity()) && Objects.equals(getEntityId(), that.getEntityId()) && Objects.equals(getApplication(), that.getApplication());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEntity(), getEntityId(), getApplication());
    }
}
