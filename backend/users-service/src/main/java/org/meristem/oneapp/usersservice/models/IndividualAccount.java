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

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@NoArgsConstructor
@Setter
@Getter
@Table("individual_account")
public class IndividualAccount extends BaseModel<String> {

    @NotNull(message = "Cannot be null")
    @Column("user_id")
    private Long userId;

    @Column("customer_id")
    private String customerId;

    private Boolean legacyCustomer;

    @Builder
    public IndividualAccount(Integer status, Long id, LocalDateTime createdDate, String createdBy, LocalDateTime lastModifiedDate,
                             String lastModifiedBy, Integer version, Long userId, String customerId, Boolean legacyCustomer) {
        super(id, createdDate, createdBy, lastModifiedDate, lastModifiedBy, version, status);
        this.userId = userId;
        this.customerId = customerId;
        this.legacyCustomer = nonNull(legacyCustomer) && legacyCustomer;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        IndividualAccount that = (IndividualAccount) o;
        return Objects.equals(getUserId(), that.getUserId())
                && Objects.equals(getCustomerId(), that.getCustomerId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserId(), getCustomerId());
    }
}
