package org.meristem.oneapp.usersservice.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
@Table("beneficiaries")
public class Beneficiaries extends BaseModel<String> {

    @NotNull(message = "Cannot be empty")
    @Column("owner_id")
    Long ownerId;

    @NotBlank(message = "Cannot be empty")
    @Column("first_name")
    String firstName;

    @Column("last_name")
    @NotBlank(message = "Cannot be empty")
    String lastName;

    @Column("beneficiary_relationship")
    @NotBlank(message = "Cannot be null")
    String beneficiaryRelationship;

    @Column("gender")
    @NotBlank(message = "Cannot be null")
    String gender;

    @Column("email")
    @NotBlank(message = "Cannot be empty")
    String email;

    @Column("phone_number")
    @NotBlank(message = "Cannot be empty")
    String phoneNumber;

    @Column("dob")
    @NotNull(message = "Cannot be null")
    LocalDate dob;

    @Column("address")
    @NotBlank(message = "Cannot be empty")
    String address;

    @Column("marital_status")
    @NotBlank(message = "Cannot be null")
    String maritalStatus;

    @Column("bank_name")
    @NotBlank(message = "Cannot be empty")
    String bankName;

    @Column("account_number")
    @NotBlank(message = "Cannot be empty")
    String accountNumber;

    @Builder
    public Beneficiaries(Integer status, Long id, LocalDateTime createdDate, String createdBy, LocalDateTime lastModifiedDate, String lastModifiedBy, Integer version, Long ownerId, String firstName,
                         String lastName, String beneficiaryRelationship, String gender, String email, String phoneNumber, LocalDate dob, String address, String maritalStatus, String bankName, String accountNumber) {
        super(id, createdDate, createdBy, lastModifiedDate, lastModifiedBy, version, status);
        this.ownerId = ownerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.beneficiaryRelationship = beneficiaryRelationship;
        this.gender = gender;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.dob = dob;
        this.address = address;
        this.maritalStatus = maritalStatus;
        this.bankName = bankName;
        this.accountNumber = accountNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Beneficiaries that = (Beneficiaries) o;
        return Objects.equals(getOwnerId(), that.getOwnerId()) && Objects.equals(getEmail(), that.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOwnerId(), getEmail());
    }
}
