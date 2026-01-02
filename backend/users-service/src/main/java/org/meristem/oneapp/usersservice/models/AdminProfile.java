package org.meristem.oneapp.usersservice.models;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.Objects;

@Table("admin_profile")
@Getter
@Setter
@NoArgsConstructor
public class AdminProfile extends BaseModel<String> {

    private Long adminId;

    @Builder
    public AdminProfile(Long id, LocalDateTime createdDate, String createdBy, LocalDateTime lastModifiedDate, String lastModifiedBy, Integer version, Long adminId) {
        super(id, createdDate, createdBy, lastModifiedDate, lastModifiedBy, version);
        this.adminId = adminId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AdminProfile that = (AdminProfile) o;
        return Objects.equals(getAdminId(), that.getAdminId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAdminId());
    }
}
