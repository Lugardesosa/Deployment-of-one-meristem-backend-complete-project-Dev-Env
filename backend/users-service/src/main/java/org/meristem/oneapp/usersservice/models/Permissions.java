package org.meristem.oneapp.usersservice.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * For granular rbac, this assigns the smallest tasks to users
 * @see Roles
 */

@NoArgsConstructor
@Setter
@Getter
@Table("permissions")
public class Permissions extends BaseModel<String> implements Serializable {

    private String name;

    public Permissions(Long id, LocalDateTime createdDate, String createdBy, LocalDateTime lastModifiedDate, String lastModifiedBy, Integer version, String name) {
        super(id, createdDate, createdBy, lastModifiedDate, lastModifiedBy, version);
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Permissions that = (Permissions) o;
        return Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getName());
    }
}
