package org.meristem.oneapp.usersservice.domains.responses;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;

import java.io.Serializable;

@Builder
public record UsersResponse(Long id, String email, String firstName, String lastName, String middleName, @JsonIgnore String password, String phoneNumber, String referralCode,
                            Boolean onboardingCompleted, String pictureUrl) implements Serializable {
}
