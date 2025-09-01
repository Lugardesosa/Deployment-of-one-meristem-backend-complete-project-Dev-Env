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
    private String fileKey;

    @Size(max = 50, message = "cannot be more than 50")
    @NotBlank(message = "cannot be null")
    private String contentType;

    @NotNull(message = "cannot be null")
    private Integer fileType;

    @NotNull(message = "cannot be null")
    private Long ownerId;

    @Builder
    public Files(Long id, LocalDateTime createdDate, String createdBy, LocalDateTime lastModifiedDate, String lastModifiedBy, Integer version, String fileKey, String contentType, Integer fileType, Long ownerId) {
        super(id, createdDate, createdBy, lastModifiedDate, lastModifiedBy, version);
        this.fileKey = fileKey;
        this.contentType = contentType;
        this.fileType = fileType;
        this.ownerId = ownerId;

    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Files files = (Files) o;
        return Objects.equals(getFileKey(), files.getFileKey()) && Objects.equals(getOwnerId(), files.getOwnerId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFileKey(), getOwnerId());
    }
}
