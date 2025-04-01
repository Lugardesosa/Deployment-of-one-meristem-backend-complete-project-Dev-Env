package org.meristem.oneapp.usersservice.models;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.Objects;


/**
 * This tables houses other pieces of information that a user can provide.
 * @see Users
 */

@NoArgsConstructor
@Setter
@Getter
@Table("user_profile")
public class UserProfile extends BaseModel<String> {

    @NotNull(message = "Cannot be null")
    private Long userId;

    @Size(max = 500, min = 5, message = "Not more than 500 and less than 5")
    private String pictureUrl;

    @Builder
    public UserProfile(Long id, LocalDateTime createdDate, String createdBy, LocalDateTime lastModifiedDate, String lastModifiedBy,
                       Integer version, Long userId, String pictureUrl) {
        super(id, createdDate, createdBy, lastModifiedDate, lastModifiedBy, version);
        this.userId = userId;
        this.pictureUrl = pictureUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserProfile that = (UserProfile) o;
        return Objects.equals(getUserId(), that.getUserId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getUserId());
    }
}
