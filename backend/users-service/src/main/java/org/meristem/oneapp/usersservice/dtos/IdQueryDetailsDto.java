package org.meristem.oneapp.usersservice.dtos;

import com.fasterxml.jackson.annotation.JsonAlias;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class IdQueryDetailsDto implements Serializable {

    private String country;

    private String dateOfBirth;

    private String photo;
    private String gender;

    private String address;

    private String countryOfBirth;
    String maritalStatus;

    private String email;
    private String firstName;
    private boolean isAlive;

    private String lastName;
    private String localAreaOfOrigin;

    private String nationality;

    private String occupation;
    private String middleName;

    private String phoneNumber;

    private String placeOfBirth;
    private String title;
    private String IdType;
    private String taxResidency;
    private String taxId;
    private String height;
    private String residenceStatus;
    private String residenceTown;
    private String residenceLga;
    private String residenceState;
    private String educationalLevel;
    private String maidenName;
    private String birthState;
    private String employmentStatus;
    private String birthCountry;
    private String birthLga;

    private String bvn;
    private String bvnHashed;
    private boolean emailVerified;
    private boolean passwordSet;

    private String employerName;
    private String sourceOfIncome;

    public String getMiddleName() {
        return StringUtils.isBlank(this.middleName) ? null : this.middleName.trim();
    }

    public String getFirstName() {
        return this.firstName.trim();
    }

    public String getLastName() {
        return this.lastName.trim();
    }
}