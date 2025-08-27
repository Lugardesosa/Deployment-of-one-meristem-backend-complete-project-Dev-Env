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

    private Long userId;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private String email;

    private String address;

    private String country;

    private String countryOfBirth;

    private LocalDate dateOfDeath;

    private LocalDate dob;

    private String document;

    private LocalDate expirationDate;

    private String fullName;

    private String gender;

    private String idNumber;

    private String idStatus;

    private String idType;

    private Boolean isAlive;

    private LocalDate issuanceDate;

    private String localAreaOfOrigin;

    private String nationality;

    private String occupation;

    private String otherNames;

    private String phoneNumber2;

    private String photo;

    private String placeOfBirth;

    private String placeOfIssuance;

    private String regionOfOrigin;

    private String secondaryIdNumber;

    private String title;

    @Builder
    public UserIdDetails(Long id, LocalDateTime createdDate, String createdBy, LocalDateTime lastModifiedDate, String lastModifiedBy, Integer version, Long userId, String firstName, String lastName, String phoneNumber,
                         String email, String address, String country, String countryOfBirth, LocalDate dateOfDeath, LocalDate dob, String document, LocalDate expirationDate, String fullName, String gender, String idNumber,
                         String idStatus, String idType, Boolean isAlive, LocalDate issuanceDate, String localAreaOfOrigin, String nationality, String occupation, String otherNames, String phoneNumber2, String photo, String placeOfBirth,
                         String placeOfIssuance, String regionOfOrigin, String secondaryIdNumber, String title) {
        super(id, createdDate, createdBy, lastModifiedDate, lastModifiedBy, version);
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
        this.country = country;
        this.countryOfBirth = countryOfBirth;
        this.dateOfDeath = dateOfDeath;
        this.dob = dob;
        this.document = document;
        this.expirationDate = expirationDate;
        this.fullName = fullName;
        this.gender = gender;
        this.idNumber = idNumber;
        this.idStatus = idStatus;
        this.idType = idType;
        this.isAlive = isAlive;
        this.issuanceDate = issuanceDate;
        this.localAreaOfOrigin = localAreaOfOrigin;
        this.nationality = nationality;
        this.occupation = occupation;
        this.otherNames = otherNames;
        this.phoneNumber2 = phoneNumber2;
        this.photo = photo;
        this.placeOfBirth = placeOfBirth;
        this.placeOfIssuance = placeOfIssuance;
        this.regionOfOrigin = regionOfOrigin;
        this.secondaryIdNumber = secondaryIdNumber;
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserIdDetails that = (UserIdDetails) o;
        return Objects.equals(getUserId(), that.getUserId()) && Objects.equals(getIdNumber(), that.getIdNumber()) && Objects.equals(getIdType(), that.getIdType());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserId(), getIdNumber(), getIdType());
    }
}
