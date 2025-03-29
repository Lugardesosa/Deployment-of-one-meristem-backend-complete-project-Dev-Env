package org.meristem.oneapp.usersservice.domains.responses;

import lombok.Builder;

import java.io.Serializable;

@Builder
public record UserResponse(Long id, String email, String firstName, String lastName, String middleName, String password, String phoneNumber, String referralCode,
                           Boolean onboardingCompleted, String pictureUrl) implements Serializable {
}
