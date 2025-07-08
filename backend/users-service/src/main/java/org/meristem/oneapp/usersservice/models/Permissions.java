package org.meristem.oneapp.usersservice.models;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a permission entity for granular RBAC (Role-Based Access Control).
 * This class assigns the smallest tasks to users.
 *
 * @see Roles
 */

@NoArgsConstructor
@Setter
@Getter
@Table("permissions")
public class Permissions extends BaseModel<String> {

    private String name;


    /**
     * Constructs a new Permissions object with the specified details.
     *
     * @param id the unique identifier of the permission
     * @param createdDate the date and time when the permission was created
     * @param createdBy the user who created the permission
     * @param lastModifiedDate the date and time when the permission was last modified
     * @param lastModifiedBy the user who last modified the permission
     * @param version the version of the permission
     * @param name the name of the permission
     */
    @Builder
    public Permissions(Long id, LocalDateTime createdDate, String createdBy, LocalDateTime lastModifiedDate, String lastModifiedBy, Integer version, String name) {
        super(id, createdDate, createdBy, lastModifiedDate, lastModifiedBy, version);
        this.name = name;
    }

    /**
     * Checks if this permission is equal to another object.
     *
     * @param o the object to compare with
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Permissions that = (Permissions) o;
        return Objects.equals(getName(), that.getName());
    }

    /**
     * Returns the hash code of this permission.
     *
     * @return the hash code of this permission
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(getName());
    }
}
