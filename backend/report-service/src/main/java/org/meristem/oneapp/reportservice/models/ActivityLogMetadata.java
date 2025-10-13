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
@Table("activity_log_metadata")
public class ActivityLogMetadata extends BaseModel<String> {

    private String mapKey;

    private String mapValue;

    private Long activityLogId;

    @Builder
    public ActivityLogMetadata(Long id, LocalDateTime createdDate, String createdBy, LocalDateTime lastModifiedDate, String lastModifiedBy, Integer version, String mapKey, String mapValue, Long activityLogId) {
        super(id, createdDate, createdBy, lastModifiedDate, lastModifiedBy, version);
        this.mapKey = mapKey;
        this.mapValue = mapValue;
        this.activityLogId = activityLogId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ActivityLogMetadata that = (ActivityLogMetadata) o;
        return Objects.equals(getActivityLogId(), that.getActivityLogId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getActivityLogId());
    }
}
