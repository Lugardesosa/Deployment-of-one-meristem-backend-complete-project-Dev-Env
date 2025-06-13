package org.meristem.oneapp.usersservice.models;

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
@Table("address")
public class Address extends BaseModel<String> {

    @NotBlank(message = "Cannot be blank")
    @Size(max = 150, message = "Not more than 150 chars")
    private String houseAddress;

    private String city;

    private String street;

    private String number;

    private String state;

    private String country;

    private String landmark;

    @NotNull(message = "Cannot be null")
    private Long userId;

//    @Builder
//    public Address(Long id, LocalDateTime createdDate, String createdBy, LocalDateTime lastModifiedDate, String lastModifiedBy, Integer version,
//                   String houseAddress, String city, String street, String number, String state, String country, String landmark, Long userId) {
//        super(id, createdDate, createdBy, lastModifiedDate, lastModifiedBy, version);
//        this.houseAddress = houseAddress;
//        this.city = city;
//        this.street = street;
//        this.number = number;
//        this.state = state;
//        this.country = country;
//        this.landmark = landmark;
//        this.userId = userId;
//    }

    @Builder
    public Address(Long id, LocalDateTime createdDate, String createdBy, LocalDateTime lastModifiedDate, String lastModifiedBy, Integer version, Integer status,
                   String houseAddress, String city, String street, String number, String state, String country, String landmark, Long userId) {
        super(id, createdDate, createdBy, lastModifiedDate, lastModifiedBy, version, status);
        this.houseAddress = houseAddress;
        this.city = city;
        this.street = street;
        this.number = number;
        this.state = state;
        this.country = country;
        this.landmark = landmark;
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(getId(), address.getId()) && Objects.equals(getUserId(), address.getUserId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUserId());
    }
}
