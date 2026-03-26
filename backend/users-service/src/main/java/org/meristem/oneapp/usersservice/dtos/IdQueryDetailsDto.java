package org.meristem.oneapp.usersservice.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
    private String idType;
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
    private boolean phoneNumberVerified;
    private boolean bvnFacialVerified;
    private boolean passwordSet;

    private String employerName;
    private String sourceOfIncome;

    private String customerId;
    private LocalDateTime createdOn;
    private LocalDateTime modifiedOn;
    private String modifiedYesNo;
    private String customerType;
    private String parentCustomerName;
    private String parentCustomerId;
    private String isMinorYesNo;
    private String isStaffYesNo;
    private String kycCompleted;
    private LocalDateTime kycCompleteOn;
    private String isBlacklistedYesNo;
    private LocalDateTime blacklistedOn;
    private String introducedBy;
    private String careOfficer;
    private String careTeam;
    private String location;
    private String customerName;
    private String otherName;
    private LocalDateTime birthDate;
    private Integer currentAge;
    private LocalDateTime weddingAnniversary;
    private String customerAddress;
    private String customerCity;
    private String customerState;
    private String customerCountry;
    private String emailAddress;
    private String phoneNumbers;
    private String bankBvn;
    private String birthLocation;
    private String externalReference;
    private String identityDocumentType;
    private String identityDocumentName;
    private String identityDocumentNo;
    private String customerJobTitle;
    private String phoneNo;
    private String genderCode;
    private String genderDescription;
    private String kycStatus;
    private String nin;
    private String isPrimaryCustomerYesNo;
    private String loginId;
    private String careOfficerId;
    private String introducerId;


    public String getMiddleName() {
        return StringUtils.isBlank(this.middleName) ? null : this.middleName.trim();
    }

    public String getFirstName() {
        return this.firstName.trim();
    }

    public String getLastName() {
        return this.lastName.trim();
    }

    public String getPhoneNumber() {
        return this.phoneNumber.replace("+", "");
    }
}