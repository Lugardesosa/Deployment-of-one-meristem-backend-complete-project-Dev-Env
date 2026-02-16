package org.meristem.oneapp.usersservice.models;


import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.Objects;

@NoArgsConstructor
@Setter
@Getter
@Table("user_pin")
public class UserPin extends BaseModel<String> {

    @NotNull(message = "Cannot be null")
    private Long userId;

    @NotNull(message = "Cannot be null")
    private String pin;

    @NotNull(message = "Cannot be null")
    private Integer failedAttempts;

    private LocalDateTime lockUntil;

    private LocalDateTime lastFailedAt;

    @Builder
    public UserPin(Integer status, Long id, LocalDateTime createdDate, String createdBy, LocalDateTime lastModifiedDate, String lastModifiedBy, Integer version, Long userId, String pin, Integer failedAttempts, LocalDateTime lockUntil, LocalDateTime lastFailedAt) {
        super(id, createdDate, createdBy, lastModifiedDate, lastModifiedBy, version, status);
        this.userId = userId;
        this.pin = pin;
        this.failedAttempts = 0;
        this.lockUntil = lockUntil;
        this.lastFailedAt = lastFailedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserPin userPin = (UserPin) o;
        return Objects.equals(getUserId(), userPin.getUserId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getUserId());
    }
}
