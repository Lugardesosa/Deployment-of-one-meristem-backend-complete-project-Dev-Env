package org.meristem.oneapp.usersservice.services;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.meristem.oneapp.usersservice.domains.enums.OtpType;
import org.meristem.oneapp.usersservice.domains.requests.CreateUserRequest;
import org.meristem.oneapp.usersservice.domains.requests.PasswordResetRequest;
import org.meristem.oneapp.usersservice.domains.responses.PasswordResetResponse;
import org.meristem.oneapp.usersservice.domains.responses.UsersResponse;
import org.meristem.oneapp.usersservice.dtos.events.UserOnboardingCompletionEvent;
import org.meristem.oneapp.usersservice.exceptionHandler.exceptions.BadRequestException;
import org.meristem.oneapp.usersservice.mappers.UsersMapping;
import org.meristem.oneapp.usersservice.models.Users;
import org.meristem.oneapp.usersservice.repositories.*;
import org.meristem.oneapp.usersservice.utils.AppUtil;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsersService {

    private final UsersRepository usersRepository;
    final UsersMapping usersMapper = UsersMapping.INSTANCE;
    private final OtpVerificationRepository otpVerificationRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    public UsersResponse createUser(@Valid CreateUserRequest userRequest) {
        if (usersRepository.existsByEmailOrPhoneNumber(userRequest.email(), userRequest.phoneNumber())) {
            throw new BadRequestException("Email or Phone number already exists.");
        }

        if (!otpVerificationRepository.existsByOtpTypeAndUserIdAndVerified(OtpType.REGISTRATION.getCode(), userRequest.email(), userRequest.phoneNumber(), true)) {
            throw new BadRequestException("Otp not verified.");
        }

        Users user = usersMapper.createUserRequestToUsers(userRequest);
        user.setPassword(passwordEncoder.encode(userRequest.password()));
        user = usersRepository.save(user);

        applicationEventPublisher.publishEvent(new UserOnboardingCompletionEvent(this, user.getId()));
        return usersMapper.usersToUserResponse(user);
    }

    public UsersResponse getUser() {
        return usersRepository.findUserDetailsByEmail(AppUtil.getLoggedInSubject());
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
