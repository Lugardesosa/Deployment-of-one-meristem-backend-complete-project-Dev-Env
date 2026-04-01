package org.meristem.oneapp.usersservice.services;

import jakarta.validation.Valid;
import org.meristem.oneapp.kafka.dtos.CreateJointCustomerDto;
import org.meristem.oneapp.kafka.dtos.CustomerAddressVerifiedDto;
import org.meristem.oneapp.kafka.dtos.KycCompletedDto;
import org.meristem.oneapp.usersservice.domains.enums.AccountType;
import org.meristem.oneapp.usersservice.domains.enums.OnboardingRequirements;
import org.meristem.oneapp.usersservice.domains.requests.*;
import org.meristem.oneapp.usersservice.domains.responses.*;
import org.meristem.oneapp.kafka.dtos.CreateCustomerDto;
import org.meristem.oneapp.usersservice.dtos.IdQueryDetailsDto;
import org.meristem.oneapp.usersservice.models.UserProfile;
import org.meristem.oneapp.usersservice.models.Users;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Interface for managing user-related operations.
 * Provides methods for creating users, updating user details, and handling authentication.
 *
 * @author Kingsley
 */
public interface IUsersService {

    /**
     * Creates initial user registration details.
     *
     * @param request the user creation request
     * @return an {@link UpdateResponse} indicating the operation result
     */
    UpdateResponse create(CreateUserRequest request);

    /**
     * Sets the password for a user after email verification.
     *
     * @param userRequest the set password request
     * @return an {@link UpdateResponse} indicating the operation result
     */
    UpdateResponse setPassword(SetPasswordRequest userRequest);

    /**
     * Saves a new user with the provided details.
     *
     * @param user the user entity
     * @return a {@link UsersResponse} containing the created user's details
     */
    UserProfile save(Users user, IdQueryDetailsDto IdQueryDetailsDto, Boolean emailVerified, AccountType accountType);

    void createJointCustomer(CreateJointCustomerDto value);

    void addressVerified(CustomerAddressVerifiedDto value);

    /**
     * Updates the user's email address.
     *
     * @param userRequest the email update request
     * @return an {@link UpdateResponse} indicating the operation result
     */
    UpdateResponse updateEmail(UpdateEmailRequest userRequest);

    UpdateResponse updateJointPrimaryEmail(UpdateEmailRequest userRequest);

    /**
     * Retrieves the currently logged-in user's details.
     *
     * @return a {@link UsersResponse} containing the user's details
     */
    UsersResponse getUser();

    /**
     * Resets the user's password using OTP verification.
     *
     * @param request the password reset request
     * @return a {@link PasswordResetResponse} indicating the operation result
     */
    PasswordResetResponse resetPassword(PasswordResetRequest request);

    /**
     * Updates the logged-in user's password.
     *
     * @param request the password update request
     * @return an {@link UpdateResponse} indicating the operation result
     */
    UpdateResponse updatePassword(UpdatePasswordRequest request);

    /**
     * Updates the logged-in user's phone number.
     *
     * @param request the phone number update request
     * @return an {@link UpdatePhoneNumberResponse} indicating the operation result
     */
    UpdatePhoneNumberResponse updatePhoneNumber(UpdatePhoneNumberRequest request);

    /**
     * Updates the logged-in user's profile image.
     *
     * @param request the image update request
     * @return an {@link UpdateImageResponse} indicating the operation result
     */
    UpdateImageResponse updateImage(UpdateImageRequest request);

    /**
     * Updates or creates the logged-in user's PIN.
     *
     * @param request the PIN request
     * @return a {@link PinResponse} indicating the operation result
     */
    PinResponse updatePin(PinRequest request);

    /**
     * Retrieves signed URLs for all available avatar images.
     *
     * @return a list of {@link SignedUrlResponse} for avatars
     */
    List<SignedUrlResponse> getAvatarUrls();

    /**
     * Deactivates the currently logged-in user's account.
     *
     * @return an {@link AccountDeactivationResponse} indicating the operation result
     */
    AccountDeactivationResponse deactivateUser();

    /**
     * Completes onboarding for the specified user.
     *
     * @param userId the user identifier
     */
    void completeUserOnboarding(KycCompletedDto dto, boolean kyc, String userId, Long productId, OnboardingRequirements onboardingRequirements);

    /**
     * Resets onboarding for the specified user.
     *
     * @param userId the user identifier
     * @param requirementId the requirement that was rejected
     */
    void resetUserOnboarding(String userId, Long requirementId);

    /**
     * Updates the logged-in user's state of origin.
     *
     * @param request the state update request
     * @return an {@link UpdateResponse} indicating the operation result
     */
    UpdateResponse updateStateOfOrigin(StateUpdateRequest request);

    /**
     * Updates the logged-in user's country of origin.
     *
     * @param request the country update request
     * @return an {@link UpdateResponse} indicating the operation result
     */
    UpdateResponse updateCountryOfOrigin(CountryUpdateRequest request);

    /**
     * Updates user instrument access status.
     *
     * @param request the user instrument request
     * @return an {@link UpdateResponse} indicating the operation result
     */
    UpdateResponse updateUserInstrument(UserInstrumentRequest request);

    /**
     * Updates investment option access status.
     *
     * @param request the option accessed request
     * @return an {@link UpdateResponse} indicating the operation result
     */
    UpdateResponse updateOptionAccessed(OptionAccessedRequest request);

//    /**
//     * Updates biometric login setting.
//     *
//     * @param request the biometric login update request
//     * @return an {@link UpdateResponse} indicating the operation result
//     */
//    UpdateResponse updateBiometricOfOrigin(BiometricLoginUpdateRequest request);

    /**
     * Updates data sharing setting for a specific instrument.
     *
     * @param request the data sharing request
     * @return an {@link UpdateResponse} indicating the operation result
     */
    UpdateResponse updateDataSharing(DataSharingRequest request);

    /**
     * Enables data sharing for all instruments.
     *
     * @return an {@link UpdateResponse} indicating the operation result
     */
    UpdateResponse updateDataSharing(ShareAllDataRequest request);

    /**
     * Updates interest-free investment preference.
     *
     * @param request the interest sharing request
     * @return an {@link UpdateResponse} indicating the operation result
     */
    UpdateResponse interestFree(InterestSharingRequest request);

    /**
     * Verifies the user's PIN.
     *
     * @param request the verify PIN request
     * @return an {@link UpdateResponse} indicating the verification result
     */
    UpdateResponse verifyPin(VerifyPinRequest request);

    /**
     * Retrieves the onboarding stage for a user.
     *
     * @param email the user's email
     * @return a {@link StageResponse} containing the current stage
     */
    StageResponse processDetails(String email);

    /**
     * Verifies the user's password.
     *
     * @param request the verify password request
     * @return an {@link UpdateResponse} indicating the verification result
     */
    UpdateResponse verifyPassword(VerifyPasswordRequest request);

    UpdateResponse updateCscs(@Valid UpdateCscsRequest request);

    UpdateResponse createSpouse(@Valid CreateSpouseRequest request);

    UpdateResponse getSecondaryUserDetails(SecondaryUserRegRequest request);

    void createCustomer(CreateCustomerDto value);

    UpdateResponse createJoint(@Valid CreateJointUserRequest userRequest);

    UpdateResponse verifyEmail(@Valid VerifyOtpRequest request);

    UpdateResponse setJointPassword(@Valid SetPasswordRequest request);

    UpdateResponse onboardOnProduct(Long userId, String email);

    List<JointAccountDetailsResponse> getJointAccountDetails();

//    UsersResponse getInvestmentInstrument();

    UpdateResponse setPasswordExisting(@Valid SetPasswordRequest request);

    UpdateResponse setJointPasswordSecondary(@Valid SetPasswordRequest request);

    @Transactional
    default UpdateResponse setPasswordInternal(SetPasswordRequest userRequest) {
        return switch (userRequest.passwordSetType()) {
            case INDIVIDUAL -> setPassword(userRequest);
            case PRIMARY -> setJointPassword(userRequest);
            case SECONDARY -> setJointPasswordSecondary(userRequest);
            case EXISTING -> setPasswordExisting(userRequest);
        };
    }

    UpdateResponse queryExistingUser(@Valid QueryExistingUserRequest request);

    UpdateResponse createUserDependent(@Valid CreateUserDependentRequest userRequest);

    void createDependent(CreateCustomerDto value);

    List<ExistingInstrumentResponse> existingInstruments();

    UsersResponse getInstruments();

//    UpdateResponse createAppJoint(CreateInAppJointAccountRequest request);

//    UpdateResponse createAppIndividual();

    List<String> getUserCustomerIds();
    Long getUserId(String customerId);

    UpdateResponse resendSec();
}
