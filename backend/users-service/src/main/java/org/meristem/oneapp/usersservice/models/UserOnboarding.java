package org.meristem.oneapp.usersservice.models;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Column;

import java.time.LocalDateTime;
import java.util.Objects;



/**
 * <p><strong>PREPOPULATED but new ones can be added</strong></p>
 *
 * This maps each requirement and user combinations, stating
 * whether they have been completed the onboarding process or not.
 * This entity is not populated upon registration, the user has to apply
 * for a given service, before the requirements for that service type is
 * used to fetch all the requirements for the service before it is then populated
 * for that user.
 * For any requirement that is shared across features like EMAIL_OTP, all features for that requirement
 * are marked as completed
 * @see FeatureRequirement
 * @see Requirements
 */
@NoArgsConstructor
@Setter
@Getter
public class UserOnboarding extends BaseModel<String> {

    @NotNull(message = "Cannot be null")
    private Long userId;

    @NotNull(message = "Cannot be null")
    private Long requirementId;

    @Column("completed")
    @NotNull(message = "Cannot be null")
    private Boolean completed;

    @Builder
    public UserOnboarding(Long id, LocalDateTime createdDate, String createdBy, LocalDateTime lastModifiedDate, String lastModifiedBy, Integer version, Long userId,
                          Long requirementId, Boolean completed) {
        super(id, createdDate, createdBy, lastModifiedDate, lastModifiedBy, version);
        this.userId = userId;
        this.requirementId = requirementId;
        this.completed = completed;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserOnboarding that = (UserOnboarding) o;
        return Objects.equals(getUserId(), that.getUserId()) && Objects.equals(getRequirementId(), that.getRequirementId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserId(), getRequirementId());
    }
}
