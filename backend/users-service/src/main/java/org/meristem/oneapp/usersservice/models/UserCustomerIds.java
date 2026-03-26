package org.meristem.oneapp.usersservice.models;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
@Table("user_customer_id")
public class UserCustomerIds extends BaseModel<String> {

    private Long userId;
    private String customerId;


    @Builder
    public UserCustomerIds(Long id, LocalDateTime createdDate, String createdBy, LocalDateTime lastModifiedDate, String lastModifiedBy, Integer version, Integer status, Long userId, String customerId) {
        super(id, createdDate, createdBy, lastModifiedDate, lastModifiedBy, version, status);
        this.userId = userId;
        this.customerId = customerId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserCustomerIds that = (UserCustomerIds) o;
        return Objects.equals(getUserId(), that.getUserId()) && Objects.equals(getCustomerId(), that.getCustomerId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserId(), getCustomerId());
    }
}
