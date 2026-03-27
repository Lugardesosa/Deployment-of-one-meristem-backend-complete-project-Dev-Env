package org.meristem.oneapp.usersservice.models;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
@Table("locations")
public class Locations extends BaseModel<String> {

    private Long userId;
    private String country;
    private String state;
    private String city;

    private BigDecimal latitude;
    private BigDecimal longitude;

    private String geohash;

    public Locations(Long id, LocalDateTime createdDate, String createdBy, LocalDateTime lastModifiedDate, String lastModifiedBy, Integer version, Integer status, Long userId, String country, String state, String city, BigDecimal latitude, BigDecimal longitude, String geohash) {
        super(id, createdDate, createdBy, lastModifiedDate, lastModifiedBy, version, status);
        this.userId = userId;
        this.country = country;
        this.state = state;
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;
        this.geohash = geohash;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Locations locations = (Locations) o;
        return Objects.equals(getId(), locations.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
