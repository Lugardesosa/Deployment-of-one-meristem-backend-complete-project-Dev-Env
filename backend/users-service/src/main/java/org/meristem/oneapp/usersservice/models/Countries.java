package org.meristem.oneapp.usersservice.models;


import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
@Table("countries")
public class Countries extends BaseModel<String> {
    
    @NotBlank(message = "Cannot be blank")
    @Column("name")
    private String name;

    @NotBlank(message = "Cannot be blank")
    @Column("nationality")
    private String nationality;

    @NotBlank(message = "Cannot be blank")
    @Column("code")
    private String code;

    @Builder
    public Countries(Integer status, Long id, LocalDateTime createdDate, String createdBy, LocalDateTime lastModifiedDate, String lastModifiedBy, Integer version, String name,  String code,
                     String nationality) {
        super(id, createdDate, createdBy, lastModifiedDate, lastModifiedBy, version, status);
        this.name = name;
        this.code = code;
        this.nationality = nationality;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Countries countries = (Countries) o;
        return Objects.equals(getName(), countries.getName()) && Objects.equals(getCode(), countries.getCode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getCode());
    }
}
