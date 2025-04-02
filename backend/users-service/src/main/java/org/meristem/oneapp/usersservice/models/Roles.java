package org.meristem.oneapp.usersservice.models;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * For broader controller, this assigns roles like ADMIN,
 * SUPER_ADMIN etc. to users
 * @see Permissions
 */
@NoArgsConstructor
@Setter
@Getter
@Table("roles")
public class Roles extends BaseModel<String> implements Serializable {

    @NotNull
    @Column("name")
    private String name;

    public Roles(Long id, LocalDateTime createdDate, String createdBy, LocalDateTime lastModifiedDate, String lastModifiedBy, Integer version, String name) {
        super(id, createdDate, createdBy, lastModifiedDate, lastModifiedBy, version);
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Roles roles = (Roles) o;
        return Objects.equals(getName(), roles.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getName());
    }
}
