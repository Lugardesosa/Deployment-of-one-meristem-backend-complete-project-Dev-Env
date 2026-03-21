package org.meristem.oneapp.usersservice.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;


/**
 * This tables houses other pieces of information that a user can provide.
 * @see Users
 */

@NoArgsConstructor
@Setter
@Getter
@Table("occupation")
public class Occupation extends BaseModel<String> {

    @Size(max = 250, min = 1, message = "Not more than 250 and less than 5")
    private String name;

    @Builder
    public Occupation(Integer status, Long id, LocalDateTime createdDate, String createdBy, LocalDateTime lastModifiedDate, String lastModifiedBy,
                      Integer version, String name) {
        super(id, createdDate, createdBy, lastModifiedDate, lastModifiedBy, version, status);
        this.name = name;
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
        Occupation that = (Occupation) o;
        return Objects.equals(getId(), that.getId());
    }

    /**
     * Returns the hash code of this UserOnboarding instance.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
