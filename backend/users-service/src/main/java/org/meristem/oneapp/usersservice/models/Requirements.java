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
 * This entity houses all the possible requirements,
 * mapping each requirement_name like BVN with its type as an integer
 * @see UserOnboarding
 */
@NoArgsConstructor
@Setter
@Getter
@Table("requirements")
public class Requirements extends BaseModel<String> {


    @NotNull(message = "displayName cannot be null")
    private String displayName;

    @NotNull(message = "requirementName cannot be null")
    private String requirementName;

    @Builder
    public Requirements(Integer status, Long id, LocalDateTime createdDate, String createdBy, LocalDateTime lastModifiedDate, String lastModifiedBy, Integer version,
                        String displayName, String requirementName) {
        super(id, createdDate, createdBy, lastModifiedDate, lastModifiedBy, version, status);
        this.displayName = displayName;
        this.requirementName = requirementName;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Requirements that = (Requirements) o;
        return Objects.equals(getDisplayName(), that.getDisplayName()) && Objects.equals(getRequirementName(), that.getRequirementName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDisplayName(), getRequirementName());
    }
}
