package org.meristem.oneapp.trustiesservice.models;

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

@NoArgsConstructor
@Getter
@Setter
@Table("files")
public class Files extends BaseModel<String> {

    @Size(max = 500, message = "cannot be more than 300")
    @NotBlank(message = "cannot be null")
    // Name of unique Key used to identify the file
    private String documentKey;

    @Size(max = 50, message = "cannot be more than 50")
    @NotBlank(message = "cannot be null")
    private String contentType;

    @NotNull(message = "cannot be null")
    private Integer fileType;

    @NotNull(message = "cannot be null")
    private Long ownerId;

    @NotNull(message = "cannot be null")
    private Long entityId;

    @NotBlank(message = "cannot be blank")
    private String entityName;

    @Builder
    public Files(Long id, LocalDateTime createdDate, String createdBy, LocalDateTime lastModifiedDate, String lastModifiedBy, Integer version, String documentKey, String contentType, Integer fileType, Long ownerId, Long entityId, String entityName) {
        super(id, createdDate, createdBy, lastModifiedDate, lastModifiedBy, version);
        this.documentKey = documentKey;
        this.contentType = contentType;
        this.fileType = fileType;
        this.ownerId = ownerId;
        this.entityId = entityId;
        this.entityName = entityName;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Files files = (Files) o;
        return Objects.equals(getDocumentKey(), files.getDocumentKey()) && Objects.equals(getOwnerId(), files.getOwnerId()) && Objects.equals(getEntityId(), files.getEntityId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDocumentKey(), getOwnerId(), getEntityId());
    }
}
