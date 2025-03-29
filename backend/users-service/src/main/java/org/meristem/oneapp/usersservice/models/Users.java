package org.meristem.oneapp.usersservice.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.Objects;


/**
 * This is the user entity, it contains just the basic information that
 * the user shares across all the services, like first_name, last_name, etc
 * A user_feature table exists in the db that is a join table for the user and feature tables;
 * @see Feature
 */
@ToString
@NoArgsConstructor
@Setter
@Getter
@Table("users")
public class Users extends BaseModel<String> {

    @Size(max = 200, min = 5, message = "Not more than 200 and less than 5")
    @NotBlank(message = "email cannot be null")
    private String email;

    @Size(max = 150, min = 1, message = "Not more than 150 and less than 1")
    @NotBlank(message = "firstName cannot be null")
    private String firstName;

    @Size(max = 150, min = 1, message = "Not more than 150 and less than 1")
    @NotBlank(message = "lastName cannot be null")
    private String lastName;

    @Size(max = 150, min = 1, message = "Not more than 150 and less than 1")
    private String middleName;

    @Size(max = 200, min = 8, message = "Not more than 200 and less than 8")
    @NotBlank(message = "lastName cannot be null")
    private String password;

    // To be saved without the '+'
    @Size(max = 50, min = 7, message = "Not more than 50 and less than 7")
    @NotBlank(message = "phoneNumber cannot be null")
    private String phoneNumber;

    @Size(max = 100, min = 1, message = "Not more than 50 and less than 1")
    private String referralCode;

    @Column("onboarding_completed")
    @NotNull(message = "onboardingCompleted cannot be null")
    private Boolean onboardingCompleted = Boolean.FALSE;

    @Builder
    public Users(Long id, LocalDateTime createdDate, String createdBy, LocalDateTime lastModifiedDate, String lastModifiedBy, Integer version, String email,
                 String firstName, String lastName, String middleName, String password, String phoneNumber, String referralCode) {
        super(id, createdDate, createdBy, lastModifiedDate, lastModifiedBy, version);
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.referralCode = referralCode;
        this.onboardingCompleted = Boolean.FALSE;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Users users = (Users) o;
        return Objects.equals(getEmail(), users.getEmail());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Objects.hashCode(getEmail());
        return hash;
    }
}
