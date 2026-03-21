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
 * Represents a role that can be assigned to users, such as ADMIN or SUPER_ADMIN.
 * This class is mapped to the "roles" table in the database.
 *
 * @see Permissions
 */
@NoArgsConstructor
@Setter
@Getter
@Table("roles")
public class Roles extends BaseModel<String> {

    @NotNull
    @Column("name")
    private String name;

    /**
     * Constructs a new Roles instance with the specified details.
     *
     * @param id the unique identifier of the role
     * @param createdDate the date and time when the role was created
     * @param createdBy the user who created the role
     * @param lastModifiedDate the date and time when the role was last modified
     * @param lastModifiedBy the user who last modified the role
     * @param version the version of the role
     * @param name the name of the role
     */
    @Builder
    public Roles(Integer status, Long id, LocalDateTime createdDate, String createdBy, LocalDateTime lastModifiedDate, String lastModifiedBy, Integer version, String name) {
        super(id, createdDate, createdBy, lastModifiedDate, lastModifiedBy, version, status);
        this.name = name;
    }

    /**
     * Checks if this role is equal to another object.
     * Two roles are considered equal if they have the same name.
     *
     * @param o the object to compare with
     * @return true if the roles are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Roles roles = (Roles) o;
        return Objects.equals(getName(), roles.getName());
    }

    /**
     * Returns the hash code of this role.
     * The hash code is based on the name of the role.
     *
     * @return the hash code of this role
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(getName());
    }
}
