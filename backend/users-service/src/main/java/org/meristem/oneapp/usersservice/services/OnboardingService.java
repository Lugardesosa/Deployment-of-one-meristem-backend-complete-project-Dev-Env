package org.meristem.oneapp.usersservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.meristem.oneapp.usersservice.domains.enums.EntityStatus;
import org.meristem.oneapp.usersservice.domains.enums.OnboardingStatus;
import org.meristem.oneapp.usersservice.domains.requests.AddressOnboardRequest;
import org.meristem.oneapp.usersservice.domains.requests.ProcessAddressRequest;
import org.meristem.oneapp.usersservice.domains.responses.AddressOnboardingResponse;
import org.meristem.oneapp.usersservice.domains.responses.UserOnboardingResponse;
import org.meristem.oneapp.usersservice.exceptionHandler.exceptions.BadRequestException;
import org.meristem.oneapp.usersservice.models.Address;
import org.meristem.oneapp.usersservice.repositories.AddressRepository;
import org.meristem.oneapp.usersservice.repositories.UserOnboardingRepository;
import org.meristem.oneapp.usersservice.repositories.UserProfileRepository;
import org.meristem.oneapp.usersservice.utils.AppUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OnboardingService {

    private final UserOnboardingRepository userOnboardingRepository;
    private final AddressRepository addressRepository;
    private final UserProfileRepository userProfileRepository;


    /**
     * Retrieves the onboarding details for a user.
     *
     * @return a list of user onboarding responses
     */
    public List<UserOnboardingResponse> getOnboardingDetails() {
        return userOnboardingRepository.findAllUserOnboardingsByUserId(AppUtil.getLoggedInUserId(), OnboardingStatus.APPROVED.getValue());
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
