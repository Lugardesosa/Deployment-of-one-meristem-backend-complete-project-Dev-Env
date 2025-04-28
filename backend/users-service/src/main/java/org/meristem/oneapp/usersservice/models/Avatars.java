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
@Table("avatars")
public class Avatars extends BaseModel<String> {

    @Size(max = 500, message = "cannot be more than 300")
    @NotBlank(message = "cannot be null")
    private String url;


    @Builder
    public Avatars(Long id, LocalDateTime createdDate, String createdBy, LocalDateTime lastModifiedDate, String lastModifiedBy, Integer version, String url) {
        super(id, createdDate, createdBy, lastModifiedDate, lastModifiedBy, version);
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Avatars avatars = (Avatars) o;
        return Objects.equals(getUrl(), avatars.getUrl());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getUrl());
    }
}
