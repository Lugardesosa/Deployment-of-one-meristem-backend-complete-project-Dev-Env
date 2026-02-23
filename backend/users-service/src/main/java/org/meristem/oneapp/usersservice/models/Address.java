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

    private Long countryId;

    private String landmark;
    private String zipOrPostalCode;

    @NotNull(message = "Cannot be null")
    private Long userId;

    // AddressVerificationMethod
    @NotNull(message = "Cannot be null")
    private Integer verificationMethod;

    private Integer utilityBillType;
    private String documentKey;

    @Builder
    public Address(Long id, LocalDateTime createdDate, String createdBy, LocalDateTime lastModifiedDate, String lastModifiedBy, Integer version, Integer status,
                   String houseAddress, String city, String street, String number, String state, Long countryId, String landmark, Long userId, Integer verificationMethod,
                   Integer utilityBillType, String documentKey, String zipOrPostalCode) {
        super(id, createdDate, createdBy, lastModifiedDate, lastModifiedBy, version, status);
        this.houseAddress = houseAddress;
        this.city = city;
        this.street = street;
        this.number = number;
        this.state = state;
        this.countryId = countryId;
        this.landmark = landmark;
        this.userId = userId;
        this.verificationMethod = verificationMethod;
        this.utilityBillType = utilityBillType;
        this.documentKey = documentKey;
        this.zipOrPostalCode = zipOrPostalCode;
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
