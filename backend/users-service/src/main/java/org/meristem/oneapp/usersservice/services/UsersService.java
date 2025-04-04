package org.meristem.oneapp.usersservice.services;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.meristem.oneapp.usersservice.domains.enums.OtpType;
import org.meristem.oneapp.usersservice.domains.requests.CreateUserRequest;
import org.meristem.oneapp.usersservice.domains.requests.PasswordResetRequest;
import org.meristem.oneapp.usersservice.domains.responses.PasswordResetResponse;
import org.meristem.oneapp.usersservice.domains.responses.UserResponse;
import org.meristem.oneapp.usersservice.exceptionHandler.exceptions.BadRequestException;
import org.meristem.oneapp.usersservice.mappers.UsersMapper;
import org.meristem.oneapp.usersservice.models.UserProfile;
import org.meristem.oneapp.usersservice.models.Users;
import org.meristem.oneapp.usersservice.repositories.OtpVerificationRepository;
import org.meristem.oneapp.usersservice.repositories.UserProfileRepository;
import org.meristem.oneapp.usersservice.repositories.UsersRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsersService {

    private final UsersRepository usersRepository;
    private final UserProfileRepository profileRepository;
    private final UsersMapper usersMapper;
    private final OtpVerificationRepository otpVerificationRepository;

    @Transactional
    public Object createUser(@Valid CreateUserRequest userRequest) {

        if (usersRepository.existsByEmailOrPhoneNumber(userRequest.email(), userRequest.phoneNumber())) {
            throw new BadRequestException("Email or Phone number already exists.");
        }
        Users user = usersMapper.createUserRequestToUsers(userRequest);
        user = usersRepository.save(user);

        if (!otpVerificationRepository.existsByOtpTypeAndUserIdAndVerified(OtpType.REGISTRATION.getCode(), user.getEmail(), user.getPhoneNumber(), true)) {
            throw new BadRequestException("Otp not verified.");
        }

        UserProfile profile = UserProfile.builder().userId(user.getId()).build();
        profileRepository.save(profile);
        return userRequest;
    }

    public UserResponse getUser(String email) {
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
