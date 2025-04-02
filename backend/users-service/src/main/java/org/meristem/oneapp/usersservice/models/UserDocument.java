package org.meristem.oneapp.usersservice.models;

import jakarta.validation.constraints.NotBlank;
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
 * This is the user document entity, it contains all the documents the given user has provided.
 * the user uploads a document and the url is persisted in this table.
 */
@NoArgsConstructor
@Setter
@Getter
@Table("user_document")
public class UserDocument extends BaseModel<String> {

    @Size(max = 200)
    @NotBlank(message = "Not null")
    private String name;

    @Size(max = 1000)
    @NotBlank(message = "Not null")
    private String url;

    @NotNull(message = "Not null")
    private Long requirementId;

    @NotNull(message = "Not null")
    private Long userId;

    @Builder
    public UserDocument(Long id, LocalDateTime createdDate, String createdBy, LocalDateTime lastModifiedDate, String lastModifiedBy, Integer version,
                        String name, String url, Long requirementId, Long userId) {
        super(id, createdDate, createdBy, lastModifiedDate, lastModifiedBy, version);
        this.name = name;
        this.url = url;
        this.requirementId = requirementId;
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserDocument that = (UserDocument) o;
        return Objects.equals(getRequirementId(), that.getRequirementId()) && Objects.equals(getUserId(), that.getUserId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRequirementId(), getUserId());
    }
}
