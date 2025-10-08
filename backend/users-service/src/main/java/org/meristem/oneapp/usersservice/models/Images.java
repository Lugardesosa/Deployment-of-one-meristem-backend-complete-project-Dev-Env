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
@Table("images")
public class Images extends BaseModel<String> {

    @Size(max = 500, message = "cannot be more than 300")
    @NotBlank(message = "cannot be null")
    private String imageKey;

    @Size(max = 50, message = "cannot be more than 50")
    @NotBlank(message = "cannot be null")
    private String contentType;

    @NotNull(message = "cannot be null")
    private Integer imageType;

    @Builder
    public Images(Long id, LocalDateTime createdDate, String createdBy, LocalDateTime lastModifiedDate, String lastModifiedBy, Integer version, String imageKey, String contentType, Integer imageType) {
        super(id, createdDate, createdBy, lastModifiedDate, lastModifiedBy, version);
        this.imageKey = imageKey;
        this.contentType = contentType;
        this.imageType = imageType;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Images avatars = (Images) o;
        return Objects.equals(getImageKey(), avatars.getImageKey());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getImageKey());
    }
}
