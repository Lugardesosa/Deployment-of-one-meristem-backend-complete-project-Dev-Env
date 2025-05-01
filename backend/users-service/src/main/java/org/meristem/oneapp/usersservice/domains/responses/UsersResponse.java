package org.meristem.oneapp.usersservice.domains.responses;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import org.springframework.data.relational.core.mapping.Column;

import java.io.Serializable;
import java.time.LocalDate;

@Builder
public record UsersResponse(Integer status, Long id, String email, String firstName, String lastName, String middleName, @JsonIgnore String password, String phoneNumber, @JsonIgnore Integer passwordAttempt,
                            @Column("avatar_url") String avatarUrl, @JsonIgnore String pin, String gender, @Column("date_of_birth")
                            LocalDate dateOfBirth, @Column("referral_code") String referralCode, @Column("onboarding_completed") Boolean onboardingCompleted) implements Serializable {

    public UsersResponse(Integer status, Long id, String email, String firstName, String lastName, String middleName, String password, String phoneNumber, Integer passwordAttempt) {
        this(status, id, email, firstName, lastName, middleName, password, phoneNumber, passwordAttempt, "", "", "", null, "", null);
    }

}
