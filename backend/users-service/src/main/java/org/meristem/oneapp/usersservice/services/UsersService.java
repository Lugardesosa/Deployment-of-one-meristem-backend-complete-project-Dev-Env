package org.meristem.oneapp.usersservice.services;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.meristem.oneapp.kafka.dtos.MessageDetailsDto;
import org.meristem.oneapp.kafka.dtos.MessageDto;
import org.meristem.oneapp.usersservice.constants.AppConstants;
import org.meristem.oneapp.usersservice.constants.KafkaTopics;
import org.meristem.oneapp.usersservice.constants.MessageSubjects;
import org.meristem.oneapp.usersservice.domains.enums.MessageMedium;
import org.meristem.oneapp.usersservice.domains.enums.MessageType;
import org.meristem.oneapp.usersservice.domains.enums.OtpType;
import org.meristem.oneapp.usersservice.domains.requests.*;
import org.meristem.oneapp.usersservice.domains.responses.*;
import org.meristem.oneapp.usersservice.dtos.events.UserOnboardingCompletionEvent;
import org.meristem.oneapp.usersservice.exceptionHandler.exceptions.BadRequestException;
import org.meristem.oneapp.usersservice.mappers.UsersMapping;
import org.meristem.oneapp.usersservice.models.Users;
import org.meristem.oneapp.usersservice.repositories.*;
import org.meristem.oneapp.usersservice.utils.AppUtil;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;

import static java.util.Objects.requireNonNull;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsersService {

    private final UsersRepository usersRepository;
    private final UsersMapping usersMapper = UsersMapping.INSTANCE;
    private final OtpVerificationRepository otpVerificationRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final KafkaSenderService<MessageDto> kafkaSenderService;
    private final UserProfileRepository userProfileRepository;
    private final AvatarsRepository avatarsRepository;
    private final CacheManager cacheManager;

    @Transactional
    public UsersResponse createUser(CreateUserRequest userRequest) {
        if (usersRepository.existsByEmailOrPhoneNumber(userRequest.email(), userRequest.phoneNumber())) {
            throw new BadRequestException("Email or Phone number already exists.");
        }

        if (!otpVerificationRepository.existsByOtpTypeAndUserIdAndVerifiedAndExpiresAtAfter(OtpType.REGISTRATION.getCode(), userRequest.email(), userRequest.phoneNumber(), true, LocalDateTime.now())) {
            throw new BadRequestException("OTP not verified or expired.");
        }

        Users user = usersMapper.createUserRequestToUsers(userRequest);
        user.setPassword(passwordEncoder.encode(userRequest.password()));
        user = usersRepository.save(user);

        otpVerificationRepository.expireTimeByCodeAndEmailOrPhone(LocalDateTime.now(), userRequest.email(), userRequest.phoneNumber(), OtpType.REGISTRATION.getCode());
        applicationEventPublisher.publishEvent(new UserOnboardingCompletionEvent(this, user.getId(), user.getFirstName()));
        return usersMapper.usersToUserResponse(user);
    }

    public UsersResponse getUser() {
        return usersRepository.findUserDetailsByEmail(AppUtil.getLoggedInSubject()).orElseThrow(() -> new BadRequestException("User not found."));
    }

    @Transactional
    public PasswordResetResponse resetPassword(PasswordResetRequest request) {

        // Ensure otp exists and not expired
        if (!otpVerificationRepository.existsByOtpTypeAndUserIdAndVerifiedAndExpiresAtAfter(OtpType.PASSWORD_RESET.getCode(), request.recipient(), request.recipient(), true, LocalDateTime.now())) {
            throw new BadRequestException("OTP not verified or expired.");
        }

        // Ensure password is not the same as the old one
        UsersResponse usersResponse = usersRepository.findUserByEmailOrPhoneNumber(request.recipient());
        if (passwordEncoder.matches(request.password(), usersResponse.password())) {
            throw new BadRequestException("Password cannot be the same as your old password.");
        }

        // Update the password and expire otp
        usersRepository.updateUsersPassword(usersResponse.email(), passwordEncoder.encode(request.password()));
        otpVerificationRepository.expireTimeByCodeAndEmailOrPhone(LocalDateTime.now(), request.recipient(), request.recipient(), OtpType.PASSWORD_RESET.getCode());

        // Notify the user about the password rest via mail
        MessageDetailsDto messageDetailsDto = MessageDetailsDto.builder().recipient(new String[]{usersResponse.email()})
                .body("Your password was changed, if you didn't initiate this, click this link.")
                .subject(MessageSubjects.PASSWORD_RESET).build();
        MessageDto messageDto = MessageDto.builder().medium(MessageMedium.EMAIL).type(MessageType.PASSWORD_RESET).message(messageDetailsDto).build();
        kafkaSenderService.send(messageDto, Map.of(KafkaHeaders.TOPIC, KafkaTopics.KAFKA_SUCCESSFUL_PASSWORD_RESET));

        return PasswordResetResponse.builder().success(true).message("Password successfully updated.").build();
    }

    @Transactional
    public UpdatePasswordResponse updatePassword(UpdatePasswordRequest request) {

        String userEmail = AppUtil.getLoggedInUserEmail();
        // Ensure password is not the same as old one
        if (passwordEncoder.matches(request.password(), usersRepository.findPasswordByEmailOrPhoneNumber(userEmail))) {
            throw new BadRequestException("Password cannot be the same as your old password.");
        }

        // Update password and return
        usersRepository.updateUsersPassword(userEmail, passwordEncoder.encode(request.password()));
        return UpdatePasswordResponse.builder().success(true).message("Password successfully updated.").build();
    }

    public UpdatePhoneNumberResponse updatePhoneNumber(UpdatePhoneNumberRequest request) {

        String userEmail = AppUtil.getLoggedInUserEmail();

        usersRepository.updateUsersPhoneNumber(request.phoneNumber(), userEmail);
        return UpdatePhoneNumberResponse.builder().status(true).message("User phone number updated").build();
    }

    public UpdateAvatarUrlResponse updateAvatarUrl(UpdateAvatarUrlRequest request) {

        Long userId = AppUtil.getLoggedInUserId();

        if (!avatarsRepository.existsByUrl(request.avatarUrl())) {
            throw new BadRequestException("Avatar URL is not valid.");
        }

        userProfileRepository.updateUsersAvatar(request.avatarUrl(), userId);
        requireNonNull(cacheManager.getCache(AppConstants.USERS_CACHE_NAME)).evict(AppUtil.getLoggedInUserEmail());
        return UpdateAvatarUrlResponse.builder().status(true).message("User avatar updated").build();
    }

    public PinResponse updatePin(PinRequest request) {

        Long userId = AppUtil.getLoggedInUserId();

        // Ensure password is not the same as old one
        if (passwordEncoder.matches(request.pin(), usersRepository.findPinByEmailOrPhoneNumber(userId))) {
            throw new BadRequestException("Pin cannot be the same as your old pin.");
        }

        // Update password and return
        userProfileRepository.updateUsersPin(passwordEncoder.encode(request.pin()), userId);
        requireNonNull(cacheManager.getCache(AppConstants.USERS_CACHE_NAME)).evict(AppUtil.getLoggedInUserEmail());
        return PinResponse.builder().status(true).message("Pin successfully updated.").build();
    }
}
