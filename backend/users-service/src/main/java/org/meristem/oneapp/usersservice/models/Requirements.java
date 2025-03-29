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
 * mapping each requirement_name like BVN with its type as an integer
 * @see FeatureRequirement
 * @see UserOnboarding
 */
@NoArgsConstructor
@Setter
@Getter
@Table("requirements")
public class Requirements extends BaseModel<String> {


    @NotNull(message = "requirementType cannot be null")
    private Integer requirementType;

    @NotNull(message = "requirementName cannot be null")
    private String requirementName;

    @Builder
    public Requirements(Long id, LocalDateTime createdDate, String createdBy, LocalDateTime lastModifiedDate, String lastModifiedBy, Integer version,
                        Integer requirementType, String requirementName) {
        super(id, createdDate, createdBy, lastModifiedDate, lastModifiedBy, version);
        this.requirementType = requirementType;
        this.requirementName = requirementName;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Requirements that = (Requirements) o;
        return Objects.equals(getRequirementType(), that.getRequirementType()) && Objects.equals(getRequirementName(), that.getRequirementName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRequirementType(), getRequirementName());
    }
}
