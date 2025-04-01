package org.meristem.oneapp.usersservice.services;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.meristem.oneapp.usersservice.domains.enums.OtpType;
import org.meristem.oneapp.usersservice.domains.enums.Requirements;
import org.meristem.oneapp.usersservice.domains.requests.CreateUserRequest;
import org.meristem.oneapp.usersservice.domains.responses.UserResponse;
import org.meristem.oneapp.usersservice.exceptionHandler.exceptions.BadRequestException;
import org.meristem.oneapp.usersservice.exceptionHandler.exceptions.ResourceNotFoundException;
import org.meristem.oneapp.usersservice.mappers.UsersMapper;
import org.meristem.oneapp.usersservice.models.OtpVerification;
import org.meristem.oneapp.usersservice.models.UserOnboarding;
import org.meristem.oneapp.usersservice.models.UserProfile;
import org.meristem.oneapp.usersservice.models.Users;
import org.meristem.oneapp.usersservice.repositories.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

        otpVerificationRepository.findByOtpTypeAndUserIdAndVerified(OtpType.REGISTRATION.getValue(), user.getEmail(), true)
                .orElseThrow(() -> new BadRequestException("Otp not verified."));

        UserProfile profile = UserProfile.builder().userId(user.getId()).build();
        profileRepository.save(profile);
        return userRequest;
    }

    public UserResponse getUser(String email) {
        return usersRepository.findByEmail(email); //.orElseThrow(() -> new ResourceNotFoundException("User not found", "user", email));
    }
}
