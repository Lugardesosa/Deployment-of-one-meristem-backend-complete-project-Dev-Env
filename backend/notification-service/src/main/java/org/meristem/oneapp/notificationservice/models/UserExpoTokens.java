package org.meristem.oneapp.notificationservice.models;

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
@Table("user_expo_tokens")
public class UserExpoTokens extends BaseModel<String> {

    private Long userId;

    private String deviceId;

    private String expoToken;

    @Builder
    public UserExpoTokens(Long id, LocalDateTime createdDate, String createdBy, LocalDateTime lastModifiedDate, String lastModifiedBy, Integer version, Long userId, String expoToken, String deviceId) {
        super(id, createdDate, createdBy, lastModifiedDate, lastModifiedBy, version);
        this.userId = userId;
        this.expoToken = expoToken;
        this.deviceId = deviceId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserExpoTokens that = (UserExpoTokens) o;
        return Objects.equals(getDeviceId(), that.getDeviceId()) && Objects.equals(getExpoToken(), that.getExpoToken());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDeviceId(), getExpoToken());
    }
}
