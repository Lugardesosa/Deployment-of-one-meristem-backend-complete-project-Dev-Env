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
@Table("one_signal_subscriptions")
public class OneSignalSubscriptions extends BaseModel<String> {

    private Long userId;

    private String subscriptionId;

    private String deviceId;

    @Builder
    public OneSignalSubscriptions(Long id, LocalDateTime createdDate, String createdBy, LocalDateTime lastModifiedDate, String lastModifiedBy, Integer version, Long userId, String subscriptionId, String deviceId) {
        super(id, createdDate, createdBy, lastModifiedDate, lastModifiedBy, version);
        this.userId = userId;
        this.subscriptionId = subscriptionId;
        this.deviceId = deviceId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        OneSignalSubscriptions that = (OneSignalSubscriptions) o;
        return Objects.equals(getUserId(), that.getUserId()) && Objects.equals(getSubscriptionId(), that.getSubscriptionId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserId(), getSubscriptionId());
    }
}
