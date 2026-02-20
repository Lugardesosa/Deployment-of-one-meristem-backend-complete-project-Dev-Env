package org.meristem.oneapp.usersservice.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.Objects;


/**
 * Represents a user in the system.
 */
@ToString
@NoArgsConstructor
@Setter
@Getter
@Table("users")
public class Users extends BaseModel<String> {

    @Size(max = 200, min = 5, message = "Not more than 200 and less than 5")
    @NotBlank(message = "recipient cannot be null")
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
    private String password;

    @Size(max = 200, message = "Not more than 200 ")
    private String middlewareCustomerId;

    // To be saved without the '08024346767'
    @Size(max = 50, min = 7, message = "Not more than 50 and less than 7")
    @NotBlank(message = "phoneNumber cannot be null")
    private String phoneNumber;

    @NotNull(message = "passwordAttempt cannot be null")
    private Integer passwordAttempt;

    /**
     * Constructs a new Users instance.
     *
     * @param id the ID of the user
     * @param createdDate the date the user was created
     * @param createdBy the user who created this user
     * @param lastModifiedDate the date the user was last modified
     * @param lastModifiedBy the user who last modified this user
     * @param version the version of the user
     * @param email the email of the user
     * @param firstName the first name of the user
     * @param lastName the last name of the user
     * @param middleName the middle name of the user
     * @param password the password of the user
     * @param phoneNumber the phone number of the user
     */
    @Builder
    public Users(Integer status, Long id, LocalDateTime createdDate, String createdBy, LocalDateTime lastModifiedDate, String lastModifiedBy, Integer version, String email,
                 String firstName, String lastName, String middleName, String password, String phoneNumber) {
        super(id, createdDate, createdBy, lastModifiedDate, lastModifiedBy, version, status);
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.passwordAttempt = 0;
        this.middlewareCustomerId = null;
    }

    /**
     * Checks if this user is equal to another object.
     * Two users are considered equal if they have the same email.
     *
     * @param o the object to compare to
     * @return true if the users are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Users users = (Users) o;
        return Objects.equals(getEmail(), users.getEmail());
    }

    /**
     * Returns a hash code value for the user.
     *
     * @return a hash code value for this user
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Objects.hashCode(getEmail());
        return hash;
    }
}
