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

@NoArgsConstructor
@Setter
@Getter
@Table("dependent_account")
public class DependentAccount extends BaseModel<String> {

    @NotNull(message = "Cannot be null")
    @Column("user_id")
    private Long userId;

    private Long parentUserId;

    private String customerId;

    @Builder
    public DependentAccount(Integer status, Long id, LocalDateTime createdDate, String createdBy, LocalDateTime lastModifiedDate,
                            String lastModifiedBy, Integer version, Long userId, String customerId, Long parentUserId) {
        super(id, createdDate, createdBy, lastModifiedDate, lastModifiedBy, version, status);
        this.userId = userId;
        this.customerId = customerId;
        this.parentUserId = parentUserId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DependentAccount that = (DependentAccount) o;
        return Objects.equals(getUserId(), that.getUserId())
                && Objects.equals(getParentUserId(), that.getParentUserId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserId(), getParentUserId());
    }
}
