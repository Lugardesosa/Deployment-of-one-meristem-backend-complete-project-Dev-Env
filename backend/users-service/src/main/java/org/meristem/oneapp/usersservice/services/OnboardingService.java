package org.meristem.oneapp.usersservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.meristem.oneapp.usersservice.constants.AppConstants;
import org.meristem.oneapp.usersservice.domains.enums.IdCardType;
import org.meristem.oneapp.usersservice.domains.enums.Requirements;
import org.meristem.oneapp.usersservice.domains.enums.EntityStatus;
import org.meristem.oneapp.usersservice.domains.requests.AddressOnboardRequest;
import org.meristem.oneapp.usersservice.domains.requests.ProcessAddressRequest;
import org.meristem.oneapp.usersservice.domains.requests.SubmitOnboardingRequest;
import org.meristem.oneapp.usersservice.domains.responses.AddressOnboardingResponse;
import org.meristem.oneapp.usersservice.domains.responses.SubmitOnboardingResponse;
import org.meristem.oneapp.usersservice.domains.responses.UserOnboardingResponse;
import org.meristem.oneapp.usersservice.exceptionHandler.exceptions.BadRequestException;
import org.meristem.oneapp.usersservice.models.Address;
import org.meristem.oneapp.usersservice.models.IdCard;
import org.meristem.oneapp.usersservice.models.UserDocument;
import org.meristem.oneapp.usersservice.repositories.*;
import org.meristem.oneapp.usersservice.utils.AppUtil;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;

@Slf4j
@Service
@RequiredArgsConstructor
public class OnboardingService {

    private final UsersRepository usersRepository;
    private final UserOnboardingRepository userOnboardingRepository;
    private final UserDocumentRepository documentRepository;
    private final AddressRepository addressRepository;
    private final IdCardRepository idCardRepository;
    private final RequirementsRepository requirementsRepository;
    private final UserProfileRepository userProfileRepository;
    private final CacheManager cacheManager;

    /**
     * Processes the onboarding of a user by completing a specific requirement.
     *
     * The method validates the request, checks for existing completions, and handles
     * the submission of documents, addresses, or ID details based on the requirement type.
     * It also updates the user's onboarding status if all requirements are completed.
     *
     * @param request the onboarding processing request containing user and requirement details
     * @return a {@link SubmitOnboardingResponse} indicating the status and message of the onboarding process
     * @throws BadRequestException if:
     *         <ul>
     *             <li>The requirement is not found or inactive</li>
     *             <li>A required document URL is missing</li>
     *             <li>The user has already completed the requirement</li>
     *             <li>An address or ID detail is missing for the respective requirement</li>
     *             <li>The user has already submitted an address</li>
     *             <li>The requirement or user does not exist during completion</li>
     *         </ul>
     */
    @Transactional
    public SubmitOnboardingResponse onboard(SubmitOnboardingRequest request) {
        String message = null;
        long userId = requireNonNull(AppUtil.getLoggedInUserId(), "User not logged in");

        org.meristem.oneapp.usersservice.models.Requirements requirements = requirementsRepository.findByIdAndStatus(request.requirementId(), EntityStatus.ACTIVE.getValue())
                .orElseThrow(() -> new BadRequestException("Requirement not found"));

        // Ensure document url is passed for non bvn verification
        if (Requirements.of(requirements.getRequirementName()) != Requirements.BVN && isNull(request.documentUrl())) {
            throw new BadRequestException("Document url is required for requirement " + requirements.getDisplayName());
        }

        // Check if user has already completed this requirement
        if (userOnboardingRepository.existsByUserIdAndRequirementIdAndCompleted(userId,
                request.requirementId(), true)) {
            throw new BadRequestException("User has already completed this requirement");
        }

        // Save document url for non bvn requirement
        if (Requirements.of(requirements.getRequirementName()) != Requirements.BVN) {
            UserDocument document = UserDocument.builder().url(request.documentUrl()).requirementId(request.requirementId())
                    .userId(userId).name(requirements.getRequirementName()).build();
            documentRepository.save(document);
        }

        // Save address for proof of address
        if (Requirements.of(requirements.getRequirementName()) == Requirements.PROOF_OF_ADDRESS) {
            if (isNull(request.addressRequest())) {
                throw new BadRequestException("Address is required");
            }

            Address address = addressRepository.findByUserId(userId).orElse(Address.builder().city(request.addressRequest().city())
                    .landmark(request.addressRequest().landmark())
                    .houseAddress(request.addressRequest().houseAddress())
                    .userId(userId).approved(EntityStatus.INACTIVE.getValue())
                    .processing(EntityStatus.INACTIVE.getValue())
                    .build());

            // Users should not be able to change their address if it is being processed (that is address is being validated
            if (address.getProcessing().equals(EntityStatus.ACTIVE.getValue())) {
                throw new BadRequestException("User has already submitted address");
            }

            // Update the address if it is not being processed
            address.setCity(request.addressRequest().city());
            address.setHouseAddress(request.addressRequest().houseAddress());
            address.setLandmark(request.addressRequest().landmark());

            addressRepository.save(address);
            message = requirements.getRequirementName() + " submission successful. You will be notified about this requirement completion in "
                    + AppConstants.ADDRESS_APPROVAL_TIME_IN_HOURS + " hours.";
        } // Save government id details
        else if (Requirements.of(requirements.getRequirementName()) == Requirements.GOVERNMENT_ISSUED_ID) {

            if (isNull(request.idRequest())) {
                throw new BadRequestException("Id details are required");
            }
            IdCardType idCardType = IdCardType.valueOf(request.idRequest().idCardType());
            if ((IdCardType.DRIVERS_LICENSE.equals(idCardType) || IdCardType.INTERNATIONAL_PASSPORT.equals(idCardType)) &&
                    isNull(request.idRequest().expiryDate())) {
                throw new BadRequestException("Expiry date is required for international passport and driver's license");
            }
            idCardRepository.save(IdCard.builder().idValue(request.idRequest().value())
                            .idCardType(idCardType.name())
                            .expiryDate(request.idRequest().expiryDate())
                            .issuedDate(request.idRequest().issuedDate())
                            .userId(userId)
                    .build());
        } // Save the user details got from their BVN
        else if (Requirements.of(requirements.getRequirementName()) == Requirements.BVN) {
            if (isNull(request.userBvnRequest())) {
                throw new BadRequestException("User BVN details are required");
            }
            userProfileRepository.updateUsersDobAndGender(request.userBvnRequest().gender(), request.userBvnRequest().dateOfBirth(), userId);
            requireNonNull(cacheManager.getCache(AppConstants.USERS_CACHE_NAME)).evict(AppUtil.getLoggedInUserEmail());
        }

        // Mark the onboarding as complemented except for proof of address that is done manually
        if (Requirements.of(requirements.getRequirementName()) != Requirements.PROOF_OF_ADDRESS) {
            if (userOnboardingRepository.completeUserOnboarding(userId, request.requirementId()) < 1) {
                throw new BadRequestException("Could not complete user onboarding");
            }
        }

        // Check if all requirement has been completed, mark the user as completed onboarding
        if (userOnboardingRepository.allRequirementsSubmitted(userId)) {
            userProfileRepository.completeOnboarding(userId);
        }

        message = isNull(message) ? requirements.getRequirementName() + " submission successfully completed.." : message;

        return SubmitOnboardingResponse.builder().documentUrl(request.documentUrl())
                .status(true).message(message).build();
    }

    /**
     * Retrieves the onboarding details for a user.
     *
     * @return a list of user onboarding responses
     */
    public List<UserOnboardingResponse> getOnboardingDetails() {
        return userOnboardingRepository.findAllUserOnboardingsByUserId(AppUtil.getLoggedInUserId(), EntityStatus.ACTIVE.getValue());
    }


    /**
     * Approves or rejects a user's address during the onboarding process.
     *
     * If the address is approved, it marks the address as approved and completes the onboarding
     * requirement for the user. If all requirements are completed, the user's onboarding status
     * is updated to completed. If the address is rejected, it resets the approval and processing
     * status, allowing the user to update their address.
     *
     * @param request the address onboarding request containing user ID, requirement ID, and approval status
     * @return an {@link AddressOnboardingResponse} indicating whether the address was approved or rejected
     */
    @Transactional
    public AddressOnboardingResponse approveAddress(AddressOnboardRequest request) {

        Address address = addressRepository.findByUserId(request.userId()).orElseThrow(() -> new BadRequestException("Address not found"));
        if (EntityStatus.INACTIVE.getValue().equals(address.getProcessing())) {
            throw new BadRequestException("Mark address for processing first");
        }
        if (request.approve()) {
            address.setApproved(EntityStatus.ACTIVE.getValue());
            addressRepository.save(address);
            userOnboardingRepository.completeUserOnboarding(request.userId(), request.requirementId());
            if (userOnboardingRepository.allRequirementsSubmitted(request.userId())) {
                userProfileRepository.completeOnboarding(request.userId());
            }
            return new AddressOnboardingResponse(true);
        } else {
            address.setApproved(EntityStatus.INACTIVE.getValue());
            address.setProcessing(EntityStatus.INACTIVE.getValue());
            addressRepository.save(address);
            // TODO: NOTIFY THE USER ABOUT IT
            return new AddressOnboardingResponse(false);
        }
    }

    /**
     * Marks a user's address as being processed during the onboarding process.
     *
     * This method updates the address status to indicate that it is under processing.
     *
     * @param request the process address request containing the user ID
     * @return an {@link AddressOnboardingResponse} indicating the processing status
     */
    public AddressOnboardingResponse processAddress(ProcessAddressRequest request) {
        // Mark user's address as being processed
        int success = addressRepository.processAddress(request.userId());
        return new AddressOnboardingResponse(success == 1);
    }
}
