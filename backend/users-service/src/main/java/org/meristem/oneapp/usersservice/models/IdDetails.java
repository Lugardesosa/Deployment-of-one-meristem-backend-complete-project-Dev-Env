package org.meristem.oneapp.usersservice.models;


import lombok.*;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@ToString
@NoArgsConstructor
@Setter
@Getter
@Table("user_id_details")
public class IdDetails extends BaseModel<String> {

    private Long fileId;
    private Long idCardId;
    private Long userId;

    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String middleName;
    private String address;
    private String city;
    private String state;
    private String country;
    private String gender;
    private String placeOfBirth;
    private LocalDate dateOfBirth;


    @Builder
    public IdDetails(Integer status, Long id, LocalDateTime createdDate, String createdBy, LocalDateTime lastModifiedDate, String lastModifiedBy, Integer version, Long fileId, Long idCardId, String firstName, String lastName, String email, String phoneNumber, String middleName, String address, String city, String state, String country, String gender, String placeOfBirth, LocalDate dateOfBirth) {
        super(id, createdDate, createdBy, lastModifiedDate, lastModifiedBy, version, status);
        this.fileId = fileId;
        this.idCardId = idCardId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.middleName = middleName;
        this.address = address;
        this.city = city;
        this.state = state;
        this.country = country;
        this.gender = gender;
        this.placeOfBirth = placeOfBirth;
        this.dateOfBirth = dateOfBirth;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        IdDetails idDetails = (IdDetails) o;
        return Objects.equals(getIdCardId(), idDetails.getIdCardId()) && Objects.equals(getUserId(), idDetails.getUserId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIdCardId(), getUserId());
    }
}
