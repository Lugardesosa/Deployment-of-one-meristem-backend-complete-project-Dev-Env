package org.meristem.oneapp.usersservice.models;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * This entity maps a user to a service offering, like
 * user1 to stock, user1 to mutual funds, etc.
 * This table is not populated upon registration, but when the user
 * applies for a particular service. One user <-> one service.
 * @see UserOnboarding
 */
@NoArgsConstructor
@Setter
@Getter
public class UserFeature extends BaseModel<String> {

    @NotNull(message = "Mot null.")
    private Long userId;

    @NotNull(message = "Mot null.")
    private Long featureTypeId;

    @Builder
    public UserFeature(Long id, LocalDateTime createdDate, String createdBy, LocalDateTime lastModifiedDate, String lastModifiedBy,
                       Integer version, Long userId, Long featureTypeId) {
        super(id, createdDate, createdBy, lastModifiedDate, lastModifiedBy, version);
        this.userId = userId;
        this.featureTypeId = featureTypeId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserFeature that = (UserFeature) o;
        return Objects.equals(getUserId(), that.getUserId()) && Objects.equals(getFeatureTypeId(), that.getFeatureTypeId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserId(), getFeatureTypeId());
    }
}
