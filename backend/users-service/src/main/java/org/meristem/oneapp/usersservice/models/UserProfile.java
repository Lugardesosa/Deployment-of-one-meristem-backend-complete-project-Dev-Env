package org.meristem.oneapp.usersservice.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import static java.util.Objects.nonNull;


/**
 * This tables houses other pieces of information that a user can provide.
 * @see Users
 */

@NoArgsConstructor
@Setter
@Getter
@Table("user_profile")
public class UserProfile extends BaseModel<String> {

    @NotNull(message = "Cannot be null")
    private Long userId;

    @Size(max = 500, min = 5, message = "Not more than 500 and less than 5")
    private String imageKey;

    private LocalDate dateOfBirth;

    private String gender;

    private String stateOfOrigin;

    private String countryOfOrigin;

    private String lgOfOrigin;

    private Integer maritalStatus;

    private String cscsNumber;

    private String chnNumber;

    @NotBlank(message = "Cannot be empty")
    @Size(max = 15, min = 1, message = "Not more than 50 and less than 1")
    private String referralCode;

    @Column("biometric_enabled")
    @NotNull(message = "biometricEnabled cannot be null")
    private Boolean biometricEnabled = Boolean.FALSE;

    @Column("interest_free_investment")
    private Boolean interestFreeInvestment;

    private String taxId;
    private String sourceOfIncome;
    private String occupation;
    private String employerName;
    private Boolean emailVerified;
    private Boolean phoneNumberVerified;
    private Boolean bvnVerified;
    private Boolean ninVerified;
    private Boolean addressVerified;

    private Boolean dataSharing;
    private Boolean marketingDataSharing;
    private Boolean aiAndAnalyticsDataSharing;

    /**
     * Constructs a new UserOnboarding instance.
     *
     * @param id the ID of the entity
     * @param createdDate the date the entity was created
     * @param createdBy the user who created the entity
     * @param lastModifiedDate the date the entity was last modified
     * @param lastModifiedBy the user who last modified the entity
     * @param version the version of the entity
     * @param userId the ID of the user
     * @param avatarUrl the users profile picture
     */
    @Builder
    public UserProfile(Integer status, Long id, LocalDateTime createdDate, String createdBy, LocalDateTime lastModifiedDate, String lastModifiedBy,
                       Integer version, Long userId, String avatarUrl, LocalDate dateOfBirth, String gender, String referralCode,
                       Boolean interestFreeInvestment, String cscsNumber, String chnNumber, Integer maritalStatus, String taxId, String occupation, String sourceOfIncome, String employerName, Boolean emailVerified,
                       Boolean dataSharing, Boolean marketingDataSharing, Boolean aiAndAnalyticsDataSharing, Boolean phoneNumberVerified) {
        super(id, createdDate, createdBy, lastModifiedDate, lastModifiedBy, version, status);
        this.userId = userId;
        this.imageKey = avatarUrl;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.referralCode = referralCode;
        this.biometricEnabled = Boolean.FALSE;
        this.interestFreeInvestment = interestFreeInvestment;
        this.chnNumber = chnNumber;
        this.cscsNumber = cscsNumber;
        this.maritalStatus = maritalStatus;
        this.taxId = taxId;
        this.occupation = occupation;
        this.sourceOfIncome = sourceOfIncome;
        this.employerName = employerName;
        this.emailVerified = emailVerified;
        this.phoneNumberVerified = phoneNumberVerified;
        // allows us to share their data and kyc with other subsidiaries
        this.dataSharing = nonNull(dataSharing) && dataSharing;
        this.marketingDataSharing = nonNull(marketingDataSharing) && marketingDataSharing;
        this.aiAndAnalyticsDataSharing = nonNull(aiAndAnalyticsDataSharing) && aiAndAnalyticsDataSharing;
    }

    /**
     * Checks if this UserOnboarding instance is equal to another object.
     *
     * @param o the object to compare with
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserProfile that = (UserProfile) o;
        return Objects.equals(getUserId(), that.getUserId());
    }

    /**
     * Returns the hash code of this UserOnboarding instance.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(getUserId());
    }
}
