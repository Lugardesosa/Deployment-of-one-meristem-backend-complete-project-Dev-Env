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
@Table("joint_account")
public class JointAccount extends BaseModel<String> {

    @NotNull(message = "Cannot be null")
    @Column("user_id")
    private Long userId;

    private String accountId;

    @Column("customer_id")
    private String customerId;

    private String accountName;

    @NotNull(message = "Cannot be null")
    private Integer role;

    @NotNull(message = "Cannot be null")
    @Column("mandate_type")
    private Integer mandateType;

    @Builder
    public JointAccount(Integer status, Long id, LocalDateTime createdDate, String createdBy, LocalDateTime lastModifiedDate,
                        String lastModifiedBy, Integer version, Long userId, String customerId, Integer role,
                        Integer mandateType, String accountName, String accountId) {
        super(id, createdDate, createdBy, lastModifiedDate, lastModifiedBy, version, status);
        this.userId = userId;
        this.customerId = customerId;
        this.role = role;
        this.mandateType = mandateType;
        this.accountName = accountName;
        this.accountId = accountId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        JointAccount that = (JointAccount) o;
        return Objects.equals(getUserId(), that.getUserId())
                && Objects.equals(getAccountId(), that.getAccountId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserId(), getAccountId());
    }
}
