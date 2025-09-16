package org.meristem.oneapp.usersservice.domains.responses;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import org.springframework.data.relational.core.mapping.Column;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Builder
public record UsersResponse(Integer status, Long id, String email, String firstName, String lastName, String middleName, @JsonIgnore String password, String phoneNumber, @JsonIgnore Integer passwordAttempt,
                            @Column("image_key") String image, @JsonIgnore String pin, String gender, @Column("date_of_birth")
                            LocalDate dateOfBirth, @Column("referral_code") String referralCode, @Column("onboarding_completed") Boolean onboardingCompleted, List<UserInstrumentResponse> userInstrumentResponses, @JsonIgnore @Column("biometric_enabled") Boolean biometricEnabled) implements Serializable {

    public UsersResponse(Integer status, Long id, String email, String firstName, String lastName, String middleName, String phoneNumber,
                         String image, String gender, LocalDate dateOfBirth, String referralCode, Boolean onboardingCompleted, List<UserInstrumentResponse> userInstrumentResponses, Boolean biometricEnabled) {
        this(status, id, email, firstName, lastName, middleName, null, phoneNumber, null, image, "", gender, dateOfBirth, referralCode, onboardingCompleted, userInstrumentResponses, biometricEnabled);
    }

    public static UsersResponse newResponse(Integer status, Long id, String email, String firstName, String lastName, String middleName, String phoneNumber,
                                     String image, String gender, LocalDate dateOfBirth, String referralCode, Boolean onboardingCompleted, List<UserInstrumentResponse> userInstrumentResponses, Boolean biometricEnabled) {
        return new UsersResponse(status, id, email, firstName, lastName, middleName, phoneNumber, image, gender, dateOfBirth, referralCode, onboardingCompleted, userInstrumentResponses, biometricEnabled);
    }

    @Builder
    public record UserInstrumentResponse(Long id, String name, Boolean accessed, String code) implements Serializable {

    }
}
