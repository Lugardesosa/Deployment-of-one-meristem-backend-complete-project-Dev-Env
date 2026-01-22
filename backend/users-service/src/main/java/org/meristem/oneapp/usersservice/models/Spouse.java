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
@Table("spouse")
public class Spouse extends BaseModel<String> {

    @NotNull(message = "Cannot be null")
    private Long userId;

    private Integer title;

    private String fullName;

    private String email;

    private Long nationalityId;

    private String phoneNumber;

    private String phoneNumberFormat;

    @Builder
    public Spouse(Long id, LocalDateTime createdDate, String createdBy, LocalDateTime lastModifiedDate, String lastModifiedBy, Integer version, Long userId, Integer title, String fullName, String email, Long nationalityId, String phoneNumber, String phoneNumberFormat) {
        super(id, createdDate, createdBy, lastModifiedDate, lastModifiedBy, version);
        this.userId = userId;
        this.title = title;
        this.fullName = fullName;
        this.email = email;
        this.nationalityId = nationalityId;
        this.phoneNumber = phoneNumber;
        this.phoneNumberFormat = phoneNumberFormat;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Spouse spouse = (Spouse) o;
        return Objects.equals(getId(), spouse.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
