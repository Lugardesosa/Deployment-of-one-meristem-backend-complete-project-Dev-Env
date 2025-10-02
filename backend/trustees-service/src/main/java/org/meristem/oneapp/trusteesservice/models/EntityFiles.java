package org.meristem.oneapp.trusteesservice.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
@Table("entity_files")
public class EntityFiles extends BaseModel<String> {

    @NotNull(message = "cannot be null")
    private Long fileId;

    @NotNull(message = "cannot be null")
    private Long entityId;

    @NotBlank(message = "cannot be blank")
    private String entityName;

    @Builder
    public EntityFiles(Long id, LocalDateTime createdDate, String createdBy, LocalDateTime lastModifiedDate, String lastModifiedBy, Integer version, Long fileId, Long entityId, String entityName) {
        super(id, createdDate, createdBy, lastModifiedDate, lastModifiedBy, version);
        this.fileId = fileId;
        this.entityId = entityId;
        this.entityName = entityName;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        EntityFiles that = (EntityFiles) o;
        return Objects.equals(getFileId(), that.getFileId()) && Objects.equals(getEntityId(), that.getEntityId()) && Objects.equals(getEntityName(), that.getEntityName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFileId(), getEntityId(), getEntityName());
    }
}
