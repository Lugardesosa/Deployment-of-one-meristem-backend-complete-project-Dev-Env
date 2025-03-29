package org.meristem.oneapp.usersservice.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;


/**
 * <p><strong>PREPOPULATED but new ones can be added</strong></p>
 *
 * This is the service_type table where all the service offerings are stored
 * This table exists so if there is ever a new service offering, it can easily
 * be added to the table.
 */
@NoArgsConstructor
@Setter
@Getter
public class FeatureType extends BaseModel<String> {

    @Size(max = 100, min = 1, message = "Not more than 100 and less than 1")
    @NotBlank(message = "name cannot be null")
    private String name;

    @Size(max = 200, min = 1, message = "Not more than 100 and less than 1")
    @NotBlank(message = "code cannot be null")
    private String code;

    @Builder
    public FeatureType(Long id, LocalDateTime createdDate, String createdBy, LocalDateTime lastModifiedDate, String lastModifiedBy,
                       Integer version, String name, String code) {
        super(id, createdDate, createdBy, lastModifiedDate, lastModifiedBy, version);
        this.name = name;
        this.code = code;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        FeatureType that = (FeatureType) o;
        return Objects.equals(getCode(), that.getCode());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getCode());
    }
}
