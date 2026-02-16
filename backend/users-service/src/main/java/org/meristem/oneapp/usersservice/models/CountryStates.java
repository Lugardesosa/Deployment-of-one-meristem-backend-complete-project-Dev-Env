package org.meristem.oneapp.usersservice.models;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Table("country_states")
public class CountryStates extends BaseModel<String> {

    @NotBlank(message = "Cannot be blank")
    @Column("name")
    private String name;

    @NotNull(message = "Cannot be null")
    @Column("country_id")
    private Long countryId;

    @Builder
    public CountryStates(Integer status, Long id, LocalDateTime createdDate, String createdBy, LocalDateTime lastModifiedDate, String lastModifiedBy, Integer version, String name, Long countryId) {
        super(id, createdDate, createdBy, lastModifiedDate, lastModifiedBy, version, status);
        this.name = name;
        this.countryId = countryId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CountryStates that = (CountryStates) o;
        return Objects.equals(getName(), that.getName()) && Objects.equals(getCountryId(), that.getCountryId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getCountryId());
    }
}
