package org.meristem.oneapp.usersservice.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.Objects;


@ToString
@NoArgsConstructor
@Setter
@Getter
@Table("next_of_kin")
public class NextOfKin extends BaseModel<String> {

    @Size(max = 150, min = 3, message = "Not more than 150 and less than 3")
    @NotBlank(message = "full-name cannot be null")
    private String fullName;

    @Size(max = 200, min = 5, message = "Not more than 200 and less than 5")
    @NotBlank(message = "email cannot be null")
    private String email;

    @Size(max = 50, min = 7, message = "Not more than 50 and less than 7")
    @NotBlank(message = "phoneNumber cannot be null")
    private String phoneNumber;

    @NotBlank(message = "lastName cannot be null")
    private String relationship;

    @NotNull(message = "User id cannot be null")
    private Long userId;

    @Builder
    public NextOfKin(Long id, LocalDateTime createdDate, String createdBy, LocalDateTime lastModifiedDate, String lastModifiedBy, Integer version,
                     String fullName, String email, String phoneNumber, String relationship, Long userId) {
        super(id, createdDate, createdBy, lastModifiedDate, lastModifiedBy, version);
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.relationship = relationship;
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        NextOfKin nextOfKin = (NextOfKin) o;
        return Objects.equals(getId(), nextOfKin.getId()) && Objects.equals(getUserId(), nextOfKin.getUserId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUserId());
    }
}
