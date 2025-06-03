package org.meristem.oneapp.usersservice.services;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.meristem.oneapp.kafka.dtos.MessageDetailsDto;
import org.meristem.oneapp.kafka.dtos.MessageDto;
import org.meristem.oneapp.usersservice.constants.AppConstants;
import org.meristem.oneapp.usersservice.constants.KafkaTopics;
import org.meristem.oneapp.usersservice.constants.MessageSubjects;
import org.meristem.oneapp.usersservice.domains.enums.*;
import org.meristem.oneapp.usersservice.domains.requests.*;
import org.meristem.oneapp.usersservice.domains.responses.*;
import org.meristem.oneapp.usersservice.exception.exceptions.BadRequestException;
import org.meristem.oneapp.usersservice.mappers.AvatarMapping;
import org.meristem.oneapp.usersservice.mappers.UsersMapping;
import org.meristem.oneapp.usersservice.models.Avatars;
import org.meristem.oneapp.usersservice.models.UserOnboarding;
import org.meristem.oneapp.usersservice.models.UserProfile;
import org.meristem.oneapp.usersservice.models.Users;
import org.meristem.oneapp.usersservice.repositories.*;
import org.meristem.oneapp.usersservice.utils.AppUtil;
import org.springframework.cache.CacheManager;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Objects.requireNonNull;

/**
 * Service class for managing user-related operations.
 * Provides methods for creating users, updating user details, and handling user authentication and profile updates.
 *
 * @author Kingsley
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UsersService {
    private final AvatarMapping avatarMapping = AvatarMapping.INSTANCE;

    private final UsersRepository usersRepository;
    private final UsersMapping usersMapper = UsersMapping.INSTANCE;
    private final OtpVerificationRepository otpVerificationRepository;
    private final PasswordEncoder passwordEncoder;
    private final KafkaSenderService kafkaSenderService;
    private final UserProfileRepository userProfileRepository;
    private final AvatarsRepository avatarsRepository;
    private final CacheManager cacheManager;
    private final UserProfileRepository profileRepository;
    private final RequirementsRepository requirementsRepository;
    private final UserOnboardingRepository userOnboardingRepository;
    private final RolesRepository rolesRepository;

    /**
     * Creates a new user after validating the request and OTP.
     *
     * @param userRequest the request containing user details
     * @return the created user's response
     * @throws BadRequestException if the email or phone number already exists or OTP is invalid/expired
     */
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

        UserProfile profile = UserProfile.builder().userId(user.getId()).referralCode(AppUtil.generateReferralCode(user.getFirstName())).build();

        profileRepository.save(profile);
        Long userId = user.getId();
        requirementsRepository.findAllByStatus(EntityStatus.ACTIVE.getValue())
                .forEach(rId -> {
                    UserOnboarding userOnboarding = UserOnboarding.builder().status(OnboardingStatus.PENDING.getValue())
                            .completed(false).userId(userId).requirementId(rId).type(RequirementType.DEFAULT.getId()).build();
                    userOnboardingRepository.save(userOnboarding);
                });
        usersRepository.saveRole(userId, rolesRepository.findIdByName(Roles.USER.getName()));
        return usersMapper.usersToUserResponse(user);
    }

    /**
     * Retrieves the currently logged-in user's details.
     *
     * @return the user's response
     * @throws BadRequestException if the user is not found
     */
    public UsersResponse getUser() {
        return usersRepository.findUserDetailsByEmail(AppUtil.getLoggedInSubject()).orElseThrow(() -> new BadRequestException("User not found."));
    }

    /**
     * Resets the user's password after validating the OTP and ensuring the new password is different.
     *
     * @param request the password reset request
     * @return the password reset response
     * @throws BadRequestException if OTP is invalid/expired or the new password matches the old one
     */
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

    /**
     * Updates the logged-in user's password after ensuring it is different from the old one.
     *
     * @param request the update password request
     * @return the update password response
     * @throws BadRequestException if the new password matches the old one
     */
    @Transactional
    public UpdatePasswordResponse updatePassword(UpdatePasswordRequest request) {

        String userEmail = AppUtil.getLoggedInUserEmail();
        String userPassword = usersRepository.findPasswordByEmailOrPhoneNumber(userEmail);

        if (!passwordEncoder.matches(request.oldPassword(), userPassword)) {
            throw new BadRequestException("Wrong oldPassword entered.");
        }

        if (passwordEncoder.matches(request.newPassword(), userPassword)) {
            throw new BadRequestException("Password cannot be the same as your old oldPassword.");
        }

        // Update password and return
        usersRepository.updateUsersPassword(userEmail, passwordEncoder.encode(request.newPassword()));
        return UpdatePasswordResponse.builder().success(true).message("Password successfully updated.").build();
    }

    /**
     * Updates the logged-in user's phone number.
     *
     * @param request the update phone number request
     * @return the update phone number response
     */
    public UpdatePhoneNumberResponse updatePhoneNumber(UpdatePhoneNumberRequest request) {

        String userEmail = AppUtil.getLoggedInUserEmail();

        usersRepository.updateUsersPhoneNumber(request.phoneNumber(), userEmail);
        return UpdatePhoneNumberResponse.builder().status(true).message("User phone number updated").build();
    }

    /**
     * Updates the logged-in user's avatar URL after validating it.
     *
     * @param request the update avatar URL request
     * @return the update avatar URL response
     * @throws BadRequestException if the avatar URL is invalid
     */
    public UpdateAvatarUrlResponse updateAvatarUrl(UpdateAvatarUrlRequest request) {

        Long userId = AppUtil.getLoggedInUserId();

        Avatars avatars = avatarsRepository.findById(request.avatarId()).orElseThrow(() -> new BadRequestException("Avatar not found."));

        userProfileRepository.updateUsersAvatar(avatars.getUrl(), userId);
        requireNonNull(cacheManager.getCache(AppConstants.USERS_CACHE_NAME)).evict(AppUtil.getLoggedInUserEmail());
        return UpdateAvatarUrlResponse.builder().status(true).message("User avatar updated").build();
    }

    /**
     * Updates the logged-in user's PIN after ensuring it is different from the old one.
     *
     * @param request the update PIN request
     * @return the update PIN response
     * @throws BadRequestException if the new PIN matches the old one
     */
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

    public List<AvatarUrls> getAvatarUrls() {
        List<AvatarUrls> avatars = new ArrayList<>();
        avatarsRepository.findAll().forEach(av ->
                avatars.add(avatarMapping.avatarsToAvatarUrls(av)));
        return avatars;
    }

    public AccountDeactivationResponse deactivateUser() {
        Long userId = AppUtil.getLoggedInUserId();
        int updated = usersRepository.updateUsersStatus(userId, UserStatus.DEACTIVATED.getValue());
        return AccountDeactivationResponse.builder().message(updated == 1 ? "Successful" : "Failed").status(updated == 1).build();
    }

    public ProfilePictureUploadResponse uploadProfilePicture(@Valid ProfilePictureUploadRequest request) {

        int updated = profileRepository.updateUsersAvatar(request.pictureUrl(), AppUtil.getLoggedInUserId());
        requireNonNull(cacheManager.getCache(AppConstants.USERS_CACHE_NAME)).evict(AppUtil.getLoggedInUserEmail());
        return ProfilePictureUploadResponse.builder().status(updated == 1 ? "True" : "False")
                .url(request.pictureUrl()).build();
    }
}
