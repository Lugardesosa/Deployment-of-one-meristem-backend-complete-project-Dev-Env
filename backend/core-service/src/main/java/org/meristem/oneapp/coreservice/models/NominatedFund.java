package org.meristem.oneapp.coreservice.models;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
@Table("nominated_fund")
public class NominatedFund extends BaseModel<String> {

    @NotBlank(message = "Cannot be blank")
    String lastName;

    @NotBlank(message = "Cannot be blank")
    String firstName;

    @NotBlank(message = "Cannot be blank")
    String email;

    @NotBlank(message = "Cannot be blank")
    String phoneNumber;

    @NotBlank(message = "Cannot be blank")
    String address;

    @NotNull(message = "Not null")
    private Long ownerId;

    @Builder
    public NominatedFund(Long id, LocalDateTime createdDate, String createdBy, LocalDateTime lastModifiedDate, String lastModifiedBy, Integer version, String lastName, String firstName, String email, String phoneNumber, String address, Long ownerId) {
        super(id, createdDate, createdBy, lastModifiedDate, lastModifiedBy, version);
        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.ownerId = ownerId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        NominatedFund that = (NominatedFund) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getOwnerId(), that.getOwnerId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getOwnerId());
    }
}
