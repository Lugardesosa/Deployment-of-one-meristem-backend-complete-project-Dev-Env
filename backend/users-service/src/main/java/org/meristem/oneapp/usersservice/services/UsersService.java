package org.meristem.oneapp.usersservice.services;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.meristem.oneapp.usersservice.domains.enums.OtpType;
import org.meristem.oneapp.usersservice.domains.enums.Status;
import org.meristem.oneapp.usersservice.domains.requests.CreateUserRequest;
import org.meristem.oneapp.usersservice.domains.requests.PasswordResetRequest;
import org.meristem.oneapp.usersservice.domains.responses.PasswordResetResponse;
import org.meristem.oneapp.usersservice.domains.responses.UsersResponse;
import org.meristem.oneapp.usersservice.exceptionHandler.exceptions.BadRequestException;
import org.meristem.oneapp.usersservice.mappers.UsersMapping;
import org.meristem.oneapp.usersservice.models.UserOnboarding;
import org.meristem.oneapp.usersservice.models.UserProfile;
import org.meristem.oneapp.usersservice.models.Users;
import org.meristem.oneapp.usersservice.repositories.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsersService {

    private final UsersRepository usersRepository;
    private final UserProfileRepository profileRepository;
    final UsersMapping usersMapper = UsersMapping.INSTANCE;
    private final OtpVerificationRepository otpVerificationRepository;
    private final RequirementsRepository requirementsRepository;
    private final UserOnboardingRepository userOnboardingRepository;

    @Transactional
    public UsersResponse createUser(@Valid CreateUserRequest userRequest) {

        if (usersRepository.existsByEmailOrPhoneNumber(userRequest.email(), userRequest.phoneNumber())) {
            throw new BadRequestException("Email or Phone number already exists.");
        }
        Users user = usersMapper.createUserRequestToUsers(userRequest);
        user = usersRepository.save(user);

        final long userId = user.getId();

        if (!otpVerificationRepository.existsByOtpTypeAndUserIdAndVerified(OtpType.REGISTRATION.getCode(), user.getEmail(), user.getPhoneNumber(), true)) {
            throw new BadRequestException("Otp not verified.");
        }

        UserProfile profile = UserProfile.builder().userId(userId).build();
        profileRepository.save(profile);
        requirementsRepository.findAllByStatus(Status.ACTIVE.getValue())
                .forEach(rId -> {
                    UserOnboarding userOnboarding = UserOnboarding.builder()
                            .completed(false).userId(userId).requirementId(rId).build();
                    userOnboardingRepository.save(userOnboarding);
                });
        log.info("Created user:======> {}", user);
        UsersResponse response = usersMapper.usersToUserResponse(user);
        log.info("Created user:------> {}", response);
        return response;
    }

    public UsersResponse getUser(String email) {
        return usersRepository.findByEmail(email); //.orElseThrow(() -> new ResourceNotFoundException("User not found", "user", recipient));
    }

    @Transactional
    public PasswordResetResponse resetPassword(PasswordResetRequest request) {

        if (!otpVerificationRepository.existsByOtpTypeAndUserIdAndVerifiedAndExpiresAtAfter(OtpType.PASSWORD_RESET.getCode(), request.recipient(), true, LocalDateTime.now())) {
            throw new BadRequestException("Otp not verified or expired.");
        }

        usersRepository.updateUsersPassword(request.password(), request.recipient());
        return PasswordResetResponse.builder().success(true).message("Password successfully updated.").build();
    }
}
