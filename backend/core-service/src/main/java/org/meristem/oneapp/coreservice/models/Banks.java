package org.meristem.oneapp.coreservice.models;

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
@Table("banks")
public class Banks extends BaseModel<String> {

    @Column("name")
    private Long name;

    @Builder
    public Banks(Long id, LocalDateTime createdDate, String createdBy, LocalDateTime lastModifiedDate, String lastModifiedBy, Integer version, Long name) {
        super(id, createdDate, createdBy, lastModifiedDate, lastModifiedBy, version);
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Banks banks = (Banks) o;
        return Objects.equals(getName(), banks.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getName());
    }
}
