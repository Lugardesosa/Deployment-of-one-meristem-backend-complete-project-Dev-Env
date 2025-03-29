package org.meristem.oneapp.usersservice.models;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.Objects;


/**
 * <p><strong>PREPOPULATED but new ones can be added</strong></p>
 *
 * This entity houses all the possible requirements,
 * mapping each requirement_type like BVN to a service_type
 * like stock. A requirement type can belong to multiple services
 * like BVN. The stage is the stage in the users onboarding process, like
 * 1, 2, 3 etc, so the user completes 1 before moving to 2 if they must follow
 * process
 */
@NoArgsConstructor
@Setter
@Getter
@Table("requirements")
public class Requirements extends BaseModel<String> {


    @NotNull(message = "requirementType cannot be null")
    private Integer requirementType;

    @NotNull(message = "requirementType cannot be null")
    private Long featureTypeId;

    @NotNull(message = "requirementStage cannot be null")
    private Integer requirementStage;

    @NotNull(message = "cannot be null")
    private Boolean required = false;

    @Builder
    public Requirements(Long id, LocalDateTime createdDate, String createdBy, LocalDateTime lastModifiedDate, String lastModifiedBy, Integer version,
                        Integer requirementType, Long featureTypeId, Integer requirementStage, Boolean required) {
        super(id, createdDate, createdBy, lastModifiedDate, lastModifiedBy, version);
        this.requirementType = requirementType;
        this.featureTypeId = featureTypeId;
        this.requirementStage = requirementStage;
        this.required = required;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Requirements that = (Requirements) o;
        return Objects.equals(getRequirementType(), that.getRequirementType()) && Objects.equals(getFeatureTypeId(), that.getFeatureTypeId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRequirementType(), getFeatureTypeId());
    }
}
