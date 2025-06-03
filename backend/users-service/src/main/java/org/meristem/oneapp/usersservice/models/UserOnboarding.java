package org.meristem.oneapp.usersservice.models;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.Objects;


/**
 * <p><strong>PREPOPULATED but new ones can be added</strong></p>
 *
 * This maps each requirement and user combinations, stating
 * whether they have been completed an onboarding process or not.
 * This entity is populated upon registration.
 * @see Requirements
 * @see Users
 */
@NoArgsConstructor
@Setter
@Getter
@Table("user_onboarding")
public class UserOnboarding extends BaseModel<String> {

    @NotNull(message = "Cannot be null")
    private Long userId;

    @NotNull(message = "Cannot be null")
    private Long requirementId;

    @Column("completed")
    @NotNull(message = "Cannot be null")
    private Boolean completed;

    @Column("type")
    @NotNull(message = "Cannot be null")
    private Integer type;

    /**
     * Constructs a new UserOnboarding instance.
     *
     * @param id the ID of the entity
     * @param createdDate the date the entity was created
     * @param createdBy the user who created the entity
     * @param lastModifiedDate the date the entity was last modified
     * @param lastModifiedBy the user who last modified the entity
     * @param version the version of the entity
     * @param userId the ID of the user
     * @param requirementId the ID of the requirement
     * @param completed whether the onboarding process is completed
     */
    @Builder
    public UserOnboarding(Long id, LocalDateTime createdDate, String createdBy, LocalDateTime lastModifiedDate, String lastModifiedBy, Integer version, Integer status, Long userId,
                          Long requirementId, Boolean completed, Integer type) {
        super(id, createdDate, createdBy, lastModifiedDate, lastModifiedBy, version, status);
        this.userId = userId;
        this.requirementId = requirementId;
        this.completed = completed;
        this.type = type;
    }

    /**
     * Checks if this UserOnboarding instance is equal to another object.
     *
     * @param o the object to compare with
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserOnboarding that = (UserOnboarding) o;
        return Objects.equals(getUserId(), that.getUserId()) && Objects.equals(getRequirementId(), that.getRequirementId());
    }

    /**
     * Returns the hash code of this UserOnboarding instance.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(getUserId(), getRequirementId());
    }
}
