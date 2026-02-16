package org.meristem.oneapp.usersservice.models;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
@Table("user_id_details")
public class UserIdDetails extends BaseModel<String> {

    private Long fileId;
    private String idType;
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
    private Boolean validated;
    private String note;


    @Builder
    public UserIdDetails(Integer status, Long userId, Long id, LocalDateTime createdDate, String createdBy, LocalDateTime lastModifiedDate, String lastModifiedBy, Integer version, Long fileId, String idType, String firstName, String lastName, String email, String phoneNumber, String middleName, String address, String city, String state, String country, String gender, String placeOfBirth, LocalDate dateOfBirth, String note) {
        super(id, createdDate, createdBy, lastModifiedDate, lastModifiedBy, version, status);
        this.fileId = fileId;
        this.userId = userId;
        this.idType = idType;
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
        this.validated = false;
        this.note = note;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserIdDetails idDetails = (UserIdDetails) o;
        return Objects.equals(getIdType(), idDetails.getIdType()) && Objects.equals(getUserId(), idDetails.getUserId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIdType(), getUserId());
    }
}
