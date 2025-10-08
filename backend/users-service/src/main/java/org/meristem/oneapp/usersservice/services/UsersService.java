package org.meristem.oneapp.usersservice.services;


import com.obs.services.model.HttpMethodEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.meristem.oneapp.kafka.dtos.KycCompletedDto;
import org.meristem.oneapp.kafka.dtos.MessageDto;
import org.meristem.oneapp.kafka.dtos.OtpVerifiedDto;
import org.meristem.oneapp.kafka.dtos.PasswordChangeDto;
import org.meristem.oneapp.usersservice.constants.AppConstants;
import org.meristem.oneapp.usersservice.constants.KafkaTopics;
import org.meristem.oneapp.usersservice.constants.MessageSubjects;
import org.meristem.oneapp.usersservice.domains.enums.*;
import org.meristem.oneapp.usersservice.domains.enums.Roles;
import org.meristem.oneapp.usersservice.domains.requests.*;
import org.meristem.oneapp.usersservice.domains.responses.*;
import org.meristem.oneapp.usersservice.exception.exceptions.BadRequestException;
import org.meristem.oneapp.usersservice.mappers.UsersMapping;
import org.meristem.oneapp.usersservice.models.*;
import org.meristem.oneapp.usersservice.repositories.*;
import org.meristem.oneapp.usersservice.utils.AppUtil;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Objects.*;

/**
 * Service class for managing user-related operations.
 * Provides methods for creating users, updating user details, and handling user authentication and profile updates.
 *
 * @author Kingsley
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UsersService {

    private final UsersRepository usersRepository;
    private final UsersMapping usersMapper = UsersMapping.INSTANCE;
    private final OtpVerificationRepository otpVerificationRepository;
    private final PasswordEncoder passwordEncoder;
    private final KafkaSenderService kafkaSenderService;
    private final UserProfileRepository userProfileRepository;
    private final ImagesRepository imagesRepository;
    private final CacheManager cacheManager;
    private final UserProfileRepository profileRepository;
    private final RequirementsRepository requirementsRepository;
    private final UserOnboardingRepository userOnboardingRepository;
    private final RolesRepository rolesRepository;
    private final HuaweiService huaweiService;
    private final GeneralRepository generalRepository;
    private final CustomRepository customRepository;

    /**
     * Creates a new user after validating the request and OTP.
     *
     * @param userRequest the request containing user details
     * @return the created user's response
     * @throws BadRequestException if the email or phone number already exists, or OTP is invalid/expired
     */
    @Transactional
    public  UsersResponse createUser(CreateUserRequest userRequest) {
        if (usersRepository.existsByEmailOrPhoneNumber(userRequest.email(), userRequest.phoneNumber())) {
            throw new BadRequestException("Email or Phone number already exists.");
        }

        Users user = usersMapper.createUserRequestToUsers(userRequest);
        user.setStatus(UserStatus.EMAIL_NOT_VERIFIED.getValue());
        user = usersRepository.save(user);

        otpVerificationRepository.expireTimeByCodeAndEmailOrPhone(LocalDateTime.now(), userRequest.email(), userRequest.phoneNumber(), MessageSubject.EMAIL_VERIFICATION.getCode());

        UserProfile profile = UserProfile.builder().userId(user.getId()).referralCode(AppUtil.generateReferralCode(user.getFirstName())).build();

        profileRepository.save(profile);
        Long userId = user.getId();
        requirementsRepository.findAllByStatus(EntityStatus.ACTIVE.getValue())
                .forEach(rId -> {
                    UserOnboarding userOnboarding = UserOnboarding.builder().status(OnboardingStatus.NOT_STARTED.getValue())
                            .completed(false).userId(userId).requirementId(rId).build();
                    userOnboardingRepository.save(userOnboarding);
                });
        customRepository.saveAll(customRepository.findAll(InvestmentInstruments.class)
                        .stream().map(i -> InstrumentAccessed.builder().userId(userId).instrumentId(i.getId()).build()).toList());
        customRepository.saveAll(customRepository.findAll(InvestmentOptions.class)
                .stream().map(i -> InvestmentOptionsAccessed.builder().userId(userId).optionId(i.getId()).build()).toList());
        usersRepository.saveRole(userId, rolesRepository.findIdByName(Roles.USER.getName()));
        return usersMapper.usersToUserResponse(user);
    }

    /**
     * Sets an initial password for a user who currently has no password.
     * If the user is found, hashes and saves the provided password, marks the profile as passwordSet,
     * and activates the account if the email is already verified.
     *
     * @param userRequest payload containing the user's email and desired password
     * @return UpdateResponse indicating whether the operation succeeded
     */


    public UpdateResponse setPassword(SetPasswordRequest userRequest) {
        Optional<Users> users = usersRepository.findOneByEmailAndPasswordIsNull(userRequest.email());
        if (users.isEmpty()) {
            return UpdateResponse.builder().success(false).message("User not found.").build();
        }
        Users user = users.get();
        user.setPassword(passwordEncoder.encode(userRequest.password()));
        usersRepository.save(user);

        Optional<UserProfile> userProfile = userProfileRepository.findByUserId(user.getId());
        userProfile.ifPresent(up -> {
            up.setPasswordSet(true);
            if (up.getEmailVerified())
                up.setStatus(UserStatus.ACTIVE.getValue());
            userProfileRepository.save(up);
        });
        return UpdateResponse.builder().success(true).message("Password successfully set.").build();
    }

    /**
     * Marks a user's email as verified and updates status/cache accordingly.
     * If the user has already set a password, their overall status is set to ACTIVE.
     * Also flags email_verified on the user profile and evicts the cache entry for the email.
     *
     * @param dto payload containing the verified email
     */
    public void emailVerified(OtpVerifiedDto dto) {
        Optional<Users> users = usersRepository.findOneByEmailAndEmailVerifiedIsNull(dto.email());
        users.ifPresent(user -> {
            if (!user.getEmail().equals(dto.email())) return;
            if (nonNull(user.getPassword()))
                user.setStatus(UserStatus.ACTIVE.getValue());
            usersRepository.save(user);
            Map<String, Object> updates = new HashMap<>();
            updates.put("email_verified", true);
            customRepository.dynamicUpdate(UserProfile.class, updates, Map.of("user_id", user.getId()));
            requireNonNull(cacheManager.getCache(AppConstants.USERS_CACHE_NAME)).evict(dto.email());
        });
    }

    /**
     * Updates the current user's email if it has not yet been verified.
     * Evicts the users cache entry for the new email.
     *
     * @param userRequest payload with the new email
     * @return UpdateResponse indicating success
     * @throws BadRequestException if the existing email is already verified
     */
    public UpdateResponse updateEmail(UpdateEmailRequest userRequest) {

        Long userId = AppUtil.getLoggedInUserId();
        if (userProfileRepository.existsByUserIdAndEmailVerified(userId, true)) {
            throw new BadRequestException("Email already verified, so cannot be updated.");
        }

        usersRepository.updateEmail(userId, userRequest.newEmail());
        requireNonNull(cacheManager.getCache(AppConstants.USERS_CACHE_NAME)).evict(userRequest.newEmail());

        return UpdateResponse.builder().success(true).message("Email successfully updated").build();
    }


    /**
     * Retrieves the currently logged-in user's details.
     *
     * @return the user's response
     * @throws BadRequestException if the user is not found
     */
    public UsersResponse getUser() {
        UsersResponse response =  usersRepository.findUserDetailsByEmail(AppUtil.getLoggedInSubject()).orElseThrow(() -> new BadRequestException("User not found."));
        String signedUrl = null;
        if (StringUtils.hasText(response.image())) {
            SignedUrlResponse signedUrlResponse = huaweiService.getSignedUrl(SignedUrlRequest.builder().method(HttpMethodEnum.GET).fileName(response.image())
                    .type(SignedUrlType.IMAGE).build());
            signedUrl = signedUrlResponse.signedUrl();
        }
        return UsersResponse.newResponse(response.status(), response.id(), response.email(), response.firstName(), response.lastName(), response.middleName(),
                response.phoneNumber(), signedUrl, response.gender(), response.dateOfBirth(), response.referralCode(), response.onboardingCompleted(), response.userInstrumentResponses(), response.userOptionResponses(), response.biometricEnabled());
    }

    /**
     * Resets the user's password after validating the OTP and ensuring the new password is different.
     *
     * @param request the password reset request
     * @return the password reset response
     * @throws BadRequestException if OTP is invalid/expired, or the new password matches the old one
     */
    @Transactional
    public PasswordResetResponse resetPassword(PasswordResetRequest request) {

        // Ensure otp exists and not expired
        if (!otpVerificationRepository.existsByOtpTypeAndUserIdAndVerifiedAndExpiresAtAfter(MessageSubject.PASSWORD_RESET.getCode(), request.recipient(), request.recipient(), true, LocalDateTime.now())) {
            throw new BadRequestException("OTP not verified or expired.");
        }

        // Ensure the password is different from the old one
        UsersResponse usersResponse = usersRepository.findUserByEmailOrPhoneNumber(request.recipient());
        if (passwordEncoder.matches(request.password(), usersResponse.password())) {
            throw new BadRequestException("Password cannot be the same as your old password.");
        }

        // Update the password and expire otp
        usersRepository.updateUsersPassword(usersResponse.email(), passwordEncoder.encode(request.password()));
        otpVerificationRepository.expireTimeByCodeAndEmailOrPhone(LocalDateTime.now(), request.recipient(), request.recipient(), MessageSubject.PASSWORD_RESET.getCode());

        // Notify the user about the password rest via mail
        notifyUserAboutPasswordChange(usersResponse.email());
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
    public UpdateResponse updatePassword(UpdatePasswordRequest request) {

        String userEmail = AppUtil.getLoggedInUserEmail();
        String userPassword = usersRepository.findPasswordByEmailOrPhoneNumber(userEmail);

        // Ensure otp exists and not expired
        if (!otpVerificationRepository.existsByOtpTypeAndUserIdAndVerifiedAndExpiresAtAfter(MessageSubject.PASSWORD_RESET.getCode(), userEmail, userEmail, true, LocalDateTime.now())) {
            throw new BadRequestException("OTP not verified or expired.");
        }

        if (!passwordEncoder.matches(request.oldPassword(), userPassword)) {
            throw new BadRequestException("Wrong oldPassword entered.");
        }

        if (passwordEncoder.matches(request.newPassword(), userPassword)) {
            throw new BadRequestException("Password cannot be the same as your old oldPassword.");
        }

        // Update password and return
        usersRepository.updateUsersPassword(userEmail, passwordEncoder.encode(request.newPassword()));
        otpVerificationRepository.expireTimeByCodeAndEmailOrPhone(LocalDateTime.now(), userEmail, userEmail, MessageSubject.PASSWORD_RESET.getCode());

        // Notify the user about the password rest via mail
        notifyUserAboutPasswordChange(userEmail);
        return UpdateResponse.builder().success(true).message("Password successfully updated.").build();
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
    public UpdateImageResponse updateImage(UpdateImageRequest request) {

        Long userId = AppUtil.getLoggedInUserId();

        String imageKey;
        if (request.imageType() == ImageType.AVATAR) {
            Images avatars = imagesRepository.findByImageKeyAndImageType(request.imageKey(), ImageType.AVATAR.getValue()).orElseThrow(() -> new BadRequestException("Avatar not found."));
            imageKey = avatars.getImageKey();
        } else {
            if (request.contentType() == null) {
                throw new BadRequestException("Content type not found.");
            }
            imageKey = request.imageKey();
            imagesRepository.save(Images.builder().imageKey(imageKey).contentType(request.contentType()).imageType(ImageType.PROFILE_PICTURE.getValue()).build());
        }

        userProfileRepository.updateUsersImage(imageKey, userId);
        requireNonNull(cacheManager.getCache(AppConstants.USERS_CACHE_NAME)).evict(AppUtil.getLoggedInUserEmail());
        return UpdateImageResponse.builder().status(true).message("User image updated").build();
    }

    /**
     * This is only for updating the user's newPin when they still know their old newPin.
     * Updates the logged-in user's PIN after ensuring it is different from the old one.
     *
     * @param request the update PIN request
     * @return the update PIN response
     * @throws BadRequestException if the new PIN matches the old one
     */
    public PinResponse updatePin(PinRequest request) {

        Long userId = AppUtil.getLoggedInUserId();

        String oldPin = usersRepository.findPinByEmailOrPhoneNumber(userId);

        if (oldPin == null && request.isNew().equals(AppConstants.IS_UPDATE_PIN)) {
            throw new BadRequestException("You need to create a pin first.");
        }

        if (request.isNew().equals(AppConstants.IS_UPDATE_PIN)) {

            if (isNull(request.oldPin())) {
                throw new BadRequestException("You must pass the old pin to update your pin.");
            }
            // Ensure old matches
            if (!passwordEncoder.matches(request.oldPin(), oldPin)) {
                throw new BadRequestException("Wrong old pin entered.");
            }

            // Ensure the newPin is different from the old one
            if (passwordEncoder.matches(request.newPin(), oldPin)) {
                throw new BadRequestException("New pin cannot be the same as your old pin.");
            }
        }

        // Update newPin and return
        userProfileRepository.updateUsersPin(passwordEncoder.encode(request.newPin()), userId);
        requireNonNull(cacheManager.getCache(AppConstants.USERS_CACHE_NAME)).evict(AppUtil.getLoggedInUserEmail());
        return PinResponse.builder().status(true).message("Pin successfully set.").build();
    }

    /**
     * Retrieves signed URLs for all available avatar images.
     * Results are cached under the "avatars" cache.
     *
     * @return list of signed URL responses for avatars
     */
    @Cacheable("avatars")
    public List<SignedUrlResponse> getAvatarUrls() {
        List<SignedUrlResponse> responses = new ArrayList<>();
        for (Images images : imagesRepository.findAllByImageType(ImageType.AVATAR.getValue())) {
            responses.add(huaweiService.getSignedUrl(SignedUrlRequest.builder().method(HttpMethodEnum.GET).fileName(images.getImageKey())
                    .type(SignedUrlType.IMAGE).build()));
        }
        return responses;
    }

    /**
     * Deactivates the currently logged-in user's account.
     *
     * @return response indicating whether the operation was successful
     */
    public AccountDeactivationResponse deactivateUser() {
        Long userId = AppUtil.getLoggedInUserId();
        int updated = usersRepository.updateUsersStatus(userId, UserStatus.DEACTIVATED.getValue());
        return AccountDeactivationResponse.builder().message(updated == 1 ? "Successful" : "Failed").status(updated == 1).build();
    }

    /**
     * Completes onboarding for the specified user if all requirements are submitted,
     * updates the profile, evicts the cache entry, and emits a KYC_COMPLETED event.
     *
     * @param userId the user identifier
     */
    public void completeUserOnboarding(String userId) {

        KycCompletedDto kycCompletedDto = usersRepository.getUserKyc(userId);
        if (userOnboardingRepository.allRequirementsSubmitted(kycCompletedDto.userId())) {
            userProfileRepository.completeOnboarding(kycCompletedDto.userId());
            requireNonNull(cacheManager.getCache(AppConstants.USERS_CACHE_NAME)).evict(kycCompletedDto.email());
            kafkaSenderService.send(kycCompletedDto, Map.of(KafkaHeaders.TOPIC, KafkaTopics.KAFKA_KYC_COMPLETED));
        }
    }

    /**
     * Resets onboarding for the specified user and marks a specific requirement as REJECTED.
     * Updates the profile status, evicts the cache entry, and emits a KYC_REJECTED event.
     *
     * @param userId the user identifier
     * @param requirementId the requirement that was rejected
     */
    public void resetUserOnboarding(String userId, Long requirementId) {

        KycCompletedDto kycCompletedDto = usersRepository.getUserKyc(userId);
        userProfileRepository.resetOnboarding(kycCompletedDto.userId());
        userOnboardingRepository.updateUserOnboardingStatus(kycCompletedDto.userId(), requirementId, OnboardingStatus.REJECTED.getValue(), false);
        requireNonNull(cacheManager.getCache(AppConstants.USERS_CACHE_NAME)).evict(kycCompletedDto.email());
        kafkaSenderService.send(kycCompletedDto, Map.of(KafkaHeaders.TOPIC, KafkaTopics.KAFKA_KYC_REJECTED));
    }

    /**
     * Updates the logged-in user's state of origin after validating it against the selected country.
     *
     * @param request payload containing the state and country identifiers
     * @return UpdateResponse indicating whether the update succeeded
     */
    public UpdateResponse updateStateOfOrigin(StateUpdateRequest request) {
        AtomicInteger updated = new AtomicInteger();
        generalRepository.findOneBy(CountryStates.class, Map.of("id", request.stateId(), "countryId", request.countryId()))
                .ifPresent(countryStates -> updated.set(userProfileRepository.updateState(AppUtil.getLoggedInUserId(), countryStates.getName())));
        return UpdateResponse.builder().success(updated.get() != 0).message(updated.get() != 0 ? "Successful" : "Failed").build();
    }

    /**
     * Updates the logged-in user's country of origin.
     *
     * @param request payload containing the country identifier
     * @return UpdateResponse indicating whether the update succeeded
     */
    public UpdateResponse updateCountryOfOrigin(CountryUpdateRequest request) {

        AtomicInteger updated = new AtomicInteger();
        generalRepository.findById(Countries.class, request.id()).ifPresent(country -> updated.set(userProfileRepository.updateCountry(AppUtil.getLoggedInUserId(), country.getName())));
        return UpdateResponse.builder().success(updated.get() != 0).message(updated.get() != 0 ? "Successful" : "Failed").build();
    }

    /**
     * Sends a password change notification email event for the specified user.
     *
     * @param userEmail recipient email address
     */
    private void notifyUserAboutPasswordChange(String userEmail) {
        PasswordChangeDto otpDto = PasswordChangeDto.builder().recipient(new String[]{userEmail})
                .body("Your password was changed, if you didn't initiate this, click this link.")
                .subject(MessageSubjects.PASSWORD_RESET).build();
        MessageDto messageDto = MessageDto.builder().medium(MessageMedium.EMAIL).isHtml(true).type(MessageType.PASSWORD_RESET).message(otpDto).classSimpleName(PasswordChangeDto.class.getSimpleName()).build();
        kafkaSenderService.send(messageDto, Map.of(KafkaHeaders.TOPIC, KafkaTopics.KAFKA_SUCCESSFUL_PASSWORD_RESET));
    }

    /**
     * Marks an investment instrument as accessed for the current user and evicts the users cache entry.
     *
     * @param request payload containing the instrument access identifier
     * @return UpdateResponse indicating whether the update succeeded
     */
    public UpdateResponse updateInstrumentAccessed(InstrumentAccessedRequest request) {

        Map<String, Object> updates = new HashMap<>();
        updates.put("accessed", true);
        int updated = customRepository.dynamicUpdate(InstrumentAccessed.class, updates, Map.of("id", request.instrumentId(), "user_id", AppUtil.getLoggedInUserId()));
        requireNonNull(cacheManager.getCache(AppConstants.USERS_CACHE_NAME)).evict(AppUtil.getLoggedInUserEmail());
        return UpdateResponse.builder().success(updated != 0).message(updated != 0 ? "Successful" : "Failed").build();
    }

    /**
     * Marks an investment option as accessed for the current user and evicts the users cache entry.
     *
     * @param request payload containing the option access identifier
     * @return UpdateResponse indicating whether the update succeeded
     */
    public UpdateResponse updateOptionAccessed(OptionAccessedRequest request) {

        Map<String, Object> updates = new HashMap<>();
        updates.put("accessed", true);
        int updated = customRepository.dynamicUpdate(InvestmentOptionsAccessed.class, updates, Map.of("id", request.optionId(), "user_id", AppUtil.getLoggedInUserId()));
        requireNonNull(cacheManager.getCache(AppConstants.USERS_CACHE_NAME)).evict(AppUtil.getLoggedInUserEmail());
        return UpdateResponse.builder().success(updated != 0).message(updated != 0 ? "Successful" : "Failed").build();
    }

    /**
     * Enables or disables biometric login for the current user and evicts the users cache entry.
     *
     * @param request payload indicating whether biometric login should be enabled
     * @return UpdateResponse indicating whether the update succeeded
     */
    public UpdateResponse updateBiometricOfOrigin(BiometricLoginUpdateRequest request) {

        Map<String, Object> updates = new HashMap<>();
        updates.put("biometric_enabled", request.biometricLogin());
        int updated = customRepository.dynamicUpdate(UserProfile.class, updates, Map.of("user_id", AppUtil.getLoggedInUserId()));
        requireNonNull(cacheManager.getCache(AppConstants.USERS_CACHE_NAME)).evict(AppUtil.getLoggedInUserEmail());
        return UpdateResponse.builder().success(updated != 0).message(updated != 0 ? "Successful" : "Failed").build();
    }
}