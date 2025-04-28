package org.meristem.oneapp.usersservice.domains.responses;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import org.springframework.data.relational.core.mapping.Column;

import java.io.Serializable;
import java.time.LocalDate;

@Builder
public record UsersResponse(Long id, String email, String firstName, String lastName, String middleName, @JsonIgnore String password, String phoneNumber,
                            @Column("avatar_url") String avatarUrl, @JsonIgnore String pin, String gender, @Column("date_of_birth")
                            LocalDate dateOfBirth, @Column("referral_code") String referralCode, @Column("onboarding_completed") Boolean onboardingCompleted) implements Serializable {
}
