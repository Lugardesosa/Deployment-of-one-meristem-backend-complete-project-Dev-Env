package org.meristem.oneapp.usersservice.services;

import jakarta.validation.Valid;
import org.meristem.oneapp.kafka.dtos.CustomerAddressVerifiedDto;
import org.meristem.oneapp.usersservice.domains.requests.*;
import org.meristem.oneapp.usersservice.domains.responses.*;
import org.meristem.oneapp.kafka.dtos.CreateCustomerDto;
import org.meristem.oneapp.usersservice.dtos.IdQueryDetailsDto;
import org.meristem.oneapp.usersservice.models.Users;

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
     * @param bvn the user's BVN
     * @return a {@link UsersResponse} containing the created user's details
     */
    UsersResponse save(Users user, IdQueryDetailsDto IdQueryDetailsDto);

    void addressVerified(CustomerAddressVerifiedDto value);

    /**
     * Updates the user's email address.
     *
     * @param userRequest the email update request
     * @return an {@link UpdateResponse} indicating the operation result
     */
    UpdateResponse updateEmail(UpdateEmailRequest userRequest);

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
    void completeUserOnboarding(String userId);

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

    void createCustomer(CreateCustomerDto value);
}
