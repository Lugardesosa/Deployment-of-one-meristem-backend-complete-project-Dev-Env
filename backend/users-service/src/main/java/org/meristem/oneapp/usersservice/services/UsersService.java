package org.meristem.oneapp.usersservice.services;


import com.obs.services.model.HttpMethodEnum;
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
import org.meristem.oneapp.usersservice.models.Images;
import org.meristem.oneapp.usersservice.models.UserOnboarding;
import org.meristem.oneapp.usersservice.models.UserProfile;
import org.meristem.oneapp.usersservice.models.Users;
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
    private final ImagesRepository imagesRepository;
    private final CacheManager cacheManager;
    private final UserProfileRepository profileRepository;
    private final RequirementsRepository requirementsRepository;
    private final UserOnboardingRepository userOnboardingRepository;
    private final RolesRepository rolesRepository;
    private final HuaweiService huaweiService;

    /**
     * Creates a new user after validating the request and OTP.
     *
     * @param userRequest the request containing user details
     * @return the created user's response
     * @throws BadRequestException if the email or phone number already exists or OTP is invalid/expired
     */
    @Transactional
    public  UsersResponse createUser(CreateUserRequest userRequest) {
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
                    UserOnboarding userOnboarding = UserOnboarding.builder().status(OnboardingStatus.NOT_STARTED.getValue())
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
        UsersResponse response =  usersRepository.findUserDetailsByEmail(AppUtil.getLoggedInSubject()).orElseThrow(() -> new BadRequestException("User not found."));
        String signedUrl = null;
        if (StringUtils.hasText(response.image())) {
            SignedUrlResponse signedUrlResponse = huaweiService.getSignedUrl(SignedUrlRequest.builder().method(HttpMethodEnum.GET).fileName(response.image())
                    .type(SignedUrlType.IMAGE).build());
            signedUrl = signedUrlResponse.signedUrl();
        }
        return UsersResponse.newResponse(response.status(), response.id(), response.email(), response.firstName(), response.lastName(), response.middleName(),
                response.phoneNumber(), signedUrl, response.gender(), response.dateOfBirth(), response.referralCode(), response.onboardingCompleted());
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
    public UpdateAvatarUrlResponse updateImage(UpdateImageRequest request) {

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
        return UpdateAvatarUrlResponse.builder().status(true).message("User image updated").build();
    }

    /**
     * This is only for updating the user's pin when they still know their old pin.
     * If thwey
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

    @Cacheable("avatars")
    public List<SignedUrlResponse> getAvatarUrls() {
        List<SignedUrlResponse> responses = new ArrayList<>();
        for (Images images : imagesRepository.findAllByImageType(ImageType.AVATAR.getValue())) {
            responses.add(huaweiService.getSignedUrl(SignedUrlRequest.builder().method(HttpMethodEnum.GET).fileName(images.getImageKey())
                    .type(SignedUrlType.IMAGE).build()));
        }
        return responses;
    }

    public AccountDeactivationResponse deactivateUser() {
        Long userId = AppUtil.getLoggedInUserId();
        int updated = usersRepository.updateUsersStatus(userId, UserStatus.DEACTIVATED.getValue());
        return AccountDeactivationResponse.builder().message(updated == 1 ? "Successful" : "Failed").status(updated == 1).build();
    }
}
