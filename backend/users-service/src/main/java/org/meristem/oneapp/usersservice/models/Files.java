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
@Table("files")
public class Files extends BaseModel<String> {

    @Size(max = 500, message = "cannot be more than 300")
    @NotBlank(message = "cannot be null")
    private String fileKey;

    @Size(max = 50, message = "cannot be more than 50")
    @NotBlank(message = "cannot be null")
    private String contentType;

    @NotNull(message = "cannot be null")
    private Integer fileType;

    // Can be null if image is an avatar
    private Long userId;

    @Builder
    public Files(Long id, LocalDateTime createdDate, String createdBy, LocalDateTime lastModifiedDate, String lastModifiedBy, Integer version, String fileKey, String contentType, Integer fileType, Long userId) {
        super(id, createdDate, createdBy, lastModifiedDate, lastModifiedBy, version);
        this.fileKey = fileKey;
        this.contentType = contentType;
        this.fileType = fileType;
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Files avatars = (Files) o;
        return Objects.equals(getFileKey(), avatars.getFileKey()) && Objects.equals(getUserId(), avatars.getUserId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getFileKey());
    }
}
