package org.meristem.oneapp.usersservice.models;


import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * This table is a many-to-many table for each feature and their requirements
 * It also tells the stage the user is required to submit the requirement and
 * whether the requirement is mandatory or not.
 * @see Feature
 * @see Requirements
 */

@Getter
@Setter
@NoArgsConstructor
@Table("feature_requirement")
public class FeatureRequirement extends BaseModel<String> implements Serializable {

    @NotNull(message = "Cannot be null")
    private Long requirementId;

    @NotNull(message = "featureId cannot be null")
    private Long featureId;

    @NotNull(message = "requirementStage cannot be null")
    private Integer requirementStage;

    @NotNull(message = "cannot be null")
    private Boolean mandatory;

    @Builder
    public FeatureRequirement(Long id, LocalDateTime createdDate, String createdBy, LocalDateTime lastModifiedDate, String lastModifiedBy, Integer version,
                              Long requirementId, Long featureId, Integer requirementStage, Boolean mandatory) {
        super(id, createdDate, createdBy, lastModifiedDate, lastModifiedBy, version);
        this.requirementId = requirementId;
        this.featureId = featureId;
        this.requirementStage = requirementStage;
        this.mandatory = mandatory;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        FeatureRequirement that = (FeatureRequirement) o;
        return Objects.equals(getRequirementId(), that.getRequirementId()) && Objects.equals(getFeatureId(), that.getFeatureId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRequirementId(), getFeatureId());
    }
}
