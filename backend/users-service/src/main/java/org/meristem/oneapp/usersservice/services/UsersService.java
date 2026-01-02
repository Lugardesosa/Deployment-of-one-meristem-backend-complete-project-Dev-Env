package org.meristem.oneapp.usersservice.services;


import com.obs.services.model.HttpMethodEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.meristem.oneapp.kafka.dtos.KycCompletedDto;
import org.meristem.oneapp.kafka.dtos.MessageDto;
import org.meristem.oneapp.kafka.dtos.PasswordChangeDto;
import org.meristem.oneapp.usersservice.config.EncryptionUtil;
import org.meristem.oneapp.usersservice.constants.AppConstants;
import org.meristem.oneapp.usersservice.constants.KafkaTopics;
import org.meristem.oneapp.usersservice.constants.MessageSubjects;
import org.meristem.oneapp.usersservice.domains.enums.*;
import org.meristem.oneapp.usersservice.domains.requests.*;
import org.meristem.oneapp.usersservice.domains.responses.*;
import org.meristem.oneapp.usersservice.exception.exceptions.BadRequestException;
import org.meristem.oneapp.usersservice.mappers.UsersMapping;
import org.meristem.oneapp.usersservice.models.*;
import org.meristem.oneapp.usersservice.repositories.*;
import org.meristem.oneapp.usersservice.utils.AppUtil;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Objects.isNull;
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
@Transactional
public class UsersService {

    private final UsersRepository usersRepository;
    private final UsersMapping usersMapper = UsersMapping.INSTANCE;
    private final OtpVerificationRepository otpVerificationRepository;
    private final PasswordEncoder passwordEncoder;
    private final KafkaSenderService kafkaSenderService;
    private final UserProfileRepository userProfileRepository;
    private final FilesRepository filesRepository;
    private final CacheManager cacheManager;
    private final UserProfileRepository profileRepository;
    private final RequirementsRepository requirementsRepository;
    private final UserOnboardingRepository userOnboardingRepository;
    private final RolesRepository rolesRepository;
    private final HuaweiService huaweiService;
    private final GeneralRepository generalRepository;
    private final CustomRepository customRepository;
    private final EncryptionUtil encryptionUtil;
    private final IdCardRepository idCardRepository;

    public  UpdateResponse create(CreateUserRequest request) {
        System.out.println(AppUtil.isAdmin());

        if (usersRepository.existsByEmailOrPhoneNumber(request.email(), request.phoneNumber())) {
            throw new BadRequestException("Email or Phone number already exists.");
        }

        String bvn = requireNonNull(encryptionUtil.encrypt(request.bvn()), "Bvn cannot be null.");

        BvnQueryResponse bvnQueryResponse = BvnQueryResponse.builder()
                .email(request.email()).firstName(request.firstName()).lastName(request.lastName())
                .phoneNumber(request.phoneNumber()).bvn(bvn)
                .build();
        requireNonNull(cacheManager.getCache(AppConstants.SIGN_UP_CACHE_NAME)).put(request.email(), bvnQueryResponse);

        return UpdateResponse.builder().success(true).message("Successful").build();
    }

    @Transactional
    public UpdateResponse setPassword(SetPasswordRequest userRequest) {

        Cache cache = requireNonNull(cacheManager.getCache(AppConstants.SIGN_UP_CACHE_NAME));
        BvnQueryResponse bvnQueryResponse = cache.get(userRequest.email(), BvnQueryResponse.class);

        if (bvnQueryResponse == null) {
            throw new AccessDeniedException("Initial sign up details not found.");
        }

        if (!bvnQueryResponse.isEmailVerified()) {
            throw new BadRequestException("Email not verified.");
        }
        Users user = usersMapper.bvnQueryResponseToUsers(bvnQueryResponse);

        user.setPassword(passwordEncoder.encode(userRequest.password()));
        save(user, bvnQueryResponse.getBvn());
        cache.evict(userRequest.email());
        return UpdateResponse.builder().success(true).message("Password successfully set.").build();
    }

    public  UsersResponse save(Users user, String bvn) {
        if (usersRepository.existsByEmailOrPhoneNumber(user.getEmail(), user.getPhoneNumber())) {
            throw new BadRequestException("Email or Phone number already exists.");
        }

        user.setStatus(UserStatus.ACTIVE.getValue());
        user = usersRepository.save(user);

        idCardRepository.save(IdCard.builder().idValue(bvn)
                .idCardType(IdCardType.BVN.getName())
                .userId(user.getId())
                .build());

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
                .stream().map(i -> UserInstrument.builder().userId(userId).instrumentId(i.getId()).build()).toList());
        customRepository.saveAll(customRepository.findAll(InvestmentOptions.class)
                .stream().map(i -> InvestmentOptionsAccessed.builder().userId(userId).optionId(i.getId()).build()).toList());
        usersRepository.saveRole(userId, rolesRepository.findIdByName(AppConstants.USER_ROLE));
        return usersMapper.usersToUserResponse(user);
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

        Cache cache = requireNonNull(cacheManager.getCache(AppConstants.SIGN_UP_CACHE_NAME));

        BvnQueryResponse response = cache.get(userRequest.oldEmail(), BvnQueryResponse.class);

        if (response == null) {
            throw new AccessDeniedException("Initial sign up details not found.");
        }
        if (userRequest.newEmail().equals(userRequest.oldEmail())) {
            throw new BadRequestException("Email cannot be the same as your current email.");
        }

        if (usersRepository.existsByEmail(userRequest.newEmail())) {
            throw new BadRequestException("Email already in use.");
        }

        response.setEmail(userRequest.newEmail());
        cache.put(response.getEmail(), response);
        cache.evict(userRequest.oldEmail());

        return UpdateResponse.builder().success(true).message("Email successfully updated").build();
    }


    /**
     * Retrieves the currently logged-in user's details.
     *
     * @return the user's response
     * @throws BadRequestException if the user is not found
     */
    public UsersResponse getUser() {
        UsersResponse response =  usersRepository.findUserDetailsById(AppUtil.getLoggedInUserId()).orElseThrow(() -> new BadRequestException("User not found."));
        String signedUrl = null;
        if (StringUtils.hasText(response.image())) {
            SignedUrlResponse signedUrlResponse = huaweiService.getSignedUrl(SignedUrlRequest.builder().method(HttpMethodEnum.GET).fileName(response.image())
                    .type(SignedUrlType.IMAGE).build());
            signedUrl = signedUrlResponse.signedUrl();
        }
        boolean allDataShared = response.userInstrumentResponses().stream().allMatch(i -> i.dataSharingAllowed() == true);
        return UsersResponse.newResponse(response.status(), response.id(), response.email(), response.firstName(), response.lastName(), response.middleName(),
                response.phoneNumber(), signedUrl, response.gender(), response.dateOfBirth(), response.referralCode(),
                response.onboardingCompleted(), response.userInstrumentResponses(), allDataShared, response.userOptionResponses(), response.biometricEnabled(), response.pinSet(),
                response.interestFreeInvestment(), response.interestFreeInvestmentSet());
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
        Users users = usersRepository.findUsersByEmailOrPhoneNumber(request.recipient(), request.recipient()).orElseThrow(() -> new BadRequestException("User not found."));
        if (passwordEncoder.matches(request.password(), users.getPassword())) {
            throw new BadRequestException("Password cannot be the same as your old password.");
        }

        // Update the password and expire otp
        usersRepository.updateUsersPassword(users.getEmail(), passwordEncoder.encode(request.password()));
        requireNonNull(cacheManager.getCache(AppConstants.USERS_CACHE_NAME)).evict(users.getId());
        otpVerificationRepository.expireTimeByCodeAndEmailOrPhone(LocalDateTime.now(), request.recipient(), request.recipient(), MessageSubject.PASSWORD_RESET.getCode());

        // Notify the user about the password rest via mail
        notifyUserAboutPasswordChange(users.getEmail());
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
        Long userId = AppUtil.getLoggedInUserId();
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
        requireNonNull(cacheManager.getCache(AppConstants.USERS_CACHE_NAME)).evict(userId);
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
        Long userId = AppUtil.getLoggedInUserId();

        usersRepository.updateUsersPhoneNumber(request.phoneNumber(), userEmail);
        requireNonNull(cacheManager.getCache(AppConstants.USERS_CACHE_NAME)).evict(userId);
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
        if (request.imageType() == FileType.AVATAR) {
            Files avatars = filesRepository.findByFileKeyAndFileType(request.imageKey(), FileType.AVATAR.getValue()).orElseThrow(() -> new BadRequestException("Avatar not found."));
            imageKey = avatars.getFileKey();
            userProfileRepository.updateUsersImage(imageKey, userId);
        } else {
            if (request.contentType() == null) {
                throw new BadRequestException("Content type not found.");
            }
            imageKey = request.imageKey();
            filesRepository.findByFileTypeAndUserId(FileType.PROFILE_PICTURE.getValue(), AppUtil.getLoggedInUserId())
                    .ifPresentOrElse(i -> {
                            },
                            () -> {
                                filesRepository.save(Files.builder().userId(userId).fileKey(imageKey).contentType(request.contentType()).fileType(FileType.PROFILE_PICTURE.getValue()).build());
                                userProfileRepository.updateUsersImage(imageKey, userId);
                            }
                    );
        }

        requireNonNull(cacheManager.getCache(AppConstants.USERS_CACHE_NAME)).evict(userId);
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

        String oldPin = usersRepository.findPinById(userId);

        if (oldPin != null && request.isNew().equals(AppConstants.IS_NEW_PIN)) {
            throw new BadRequestException("Pin has already been created for this account, you should update pin instead.");
        }

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
        requireNonNull(cacheManager.getCache(AppConstants.USERS_CACHE_NAME)).evict(userId);
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
        for (Files images : filesRepository.findAllByFileType(FileType.AVATAR.getValue())) {
            responses.add(huaweiService.getSignedUrl(SignedUrlRequest.builder().method(HttpMethodEnum.GET).fileName(images.getFileKey())
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
        String userEmail = AppUtil.getLoggedInUserEmail();
        Long userId = AppUtil.getLoggedInUserId();
        int updated = usersRepository.updateUsersStatus(userEmail, UserStatus.DEACTIVATED.getValue());
        requireNonNull(cacheManager.getCache(AppConstants.USERS_CACHE_NAME)).evict(userId);
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
            requireNonNull(cacheManager.getCache(AppConstants.USERS_CACHE_NAME)).evict(kycCompletedDto.userId());
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
        requireNonNull(cacheManager.getCache(AppConstants.USERS_CACHE_NAME)).evict(kycCompletedDto.userId());
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
    public UpdateResponse updateUserInstrument(UserInstrumentRequest request) {

        Map<String, Object> updates = new HashMap<>();
        updates.put("accessed", true);
        return getUpdateResponse(updates, request.instrumentId());
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
        int updated = customRepository.dynamicUpdate(InvestmentOptionsAccessed.class, updates, Map.of("option_id", request.optionId(), "user_id", AppUtil.getLoggedInUserId()));
        clearUsersCache();
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
        return getUpdateResponse(updates);
    }


    public UpdateResponse updateDataSharing(DataSharingRequest request) {

        Map<String, Object> updates = new HashMap<>();
        updates.put("data_sharing_allowed", request.dataSharing());
        return getUpdateResponse(updates, request.instrumentId());
    }

    private UpdateResponse getUpdateResponse(Map<String, Object> updates, Long aLong) {
        int updated = customRepository.dynamicUpdate(UserInstrument.class, updates, Map.of("instrument_id", aLong, "user_id", AppUtil.getLoggedInUserId()));
        clearUsersCache();
        return UpdateResponse.builder().success(updated != 0).message(updated != 0 ? "Successful" : "Failed").build();
    }

    private void clearUsersCache() {
        requireNonNull(cacheManager.getCache(AppConstants.USERS_CACHE_NAME)).evict(AppUtil.getLoggedInUserId());
    }

    public UpdateResponse updateDataSharing() {

        Map<String, Object> updates = new HashMap<>();
        updates.put("data_sharing_allowed", true);
        int updated = customRepository.dynamicUpdate(UserInstrument.class, updates, Map.of("user_id", AppUtil.getLoggedInUserId()));
        clearUsersCache();
        return UpdateResponse.builder().success(updated != 0).message(updated != 0 ? "Successful" : "Failed").build();
    }

    public UpdateResponse interestFree(InterestSharingRequest request) {

        Map<String, Object> updates = new HashMap<>();
        updates.put("interest_free_investment", request.wantInterest());
        return getUpdateResponse(updates);
    }

    private UpdateResponse getUpdateResponse(Map<String, Object> updates) {
        int updated = customRepository.dynamicUpdate(UserProfile.class, updates, Map.of("user_id", AppUtil.getLoggedInUserId()));
        clearUsersCache();
        return UpdateResponse.builder().success(updated != 0).message(updated != 0 ? "Successful" : "Failed").build();
    }

    public UpdateResponse verifyPin(VerifyPinRequest request) {

        boolean matches = passwordEncoder.matches(request.pin(), usersRepository
                .findPinById(AppUtil.getLoggedInUserId()));
        return UpdateResponse.builder().success(matches).message(matches ? "Pin verified" : "Invalid pin").build();
    }

    public StageResponse processDetails(String email) {

        Cache cache = requireNonNull(cacheManager.getCache(AppConstants.SIGN_UP_CACHE_NAME));
        BvnQueryResponse bvnQueryResponse = cache.get(email, BvnQueryResponse.class);

        if (bvnQueryResponse == null) {
            throw new AccessDeniedException("Initial sign up details not found.");
        }

        if (!bvnQueryResponse.isEmailVerified()) {
            return new StageResponse(OnboardingStage.EMAIL);
        }
        return new StageResponse(OnboardingStage.PASSWORD);
    }

    public UpdateResponse verifyPassword(VerifyPasswordRequest request) {

        boolean matches = passwordEncoder.matches(request.password(), usersRepository
                .findPasswordById(AppUtil.getLoggedInUserId()));
        return UpdateResponse.builder().success(matches).message(matches ? "Password verified" : "Invalid Password").build();
    }
}