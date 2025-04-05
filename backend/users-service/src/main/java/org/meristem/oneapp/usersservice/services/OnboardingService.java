package org.meristem.oneapp.usersservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.meristem.oneapp.usersservice.domains.enums.Requirements;
import org.meristem.oneapp.usersservice.domains.enums.Status;
import org.meristem.oneapp.usersservice.domains.requests.SubmitOnboardingRequest;
import org.meristem.oneapp.usersservice.domains.responses.SubmitOnboardingResponse;
import org.meristem.oneapp.usersservice.domains.responses.UserOnboardingResponse;
import org.meristem.oneapp.usersservice.exceptionHandler.exceptions.BadRequestException;
import org.meristem.oneapp.usersservice.models.Address;
import org.meristem.oneapp.usersservice.models.UserDocument;
import org.meristem.oneapp.usersservice.repositories.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.Objects.isNull;

@Slf4j
@Service
@RequiredArgsConstructor
public class OnboardingService {

    private final UsersRepository usersRepository;
    private final UserOnboardingRepository userOnboardingRepository;
    private final UserDocumentRepository documentRepository;
    private final AddressRepository addressRepository;
    private final RequirementsRepository requirementsRepository;


    /**
     * Completes an onboarding requirement for a user.
     *
     * @param request the onboarding processing request containing user, and requirement details
     * @return the response indicating the status of the onboarding completion process
     * @throws BadRequestException if the requirement is already completed
     */
    @Transactional
    public SubmitOnboardingResponse onboard(SubmitOnboardingRequest request) {

        // TODO: MAKE ACCESSIBLE ONLY BY LOGGED IN USERS AND USER ID SHOULD BE GOTTEN FROM SECURITY CONTEXT

        org.meristem.oneapp.usersservice.models.Requirements requirements = requirementsRepository.findByIdAndStatus(request.requirementId(), Status.ACTIVE.getValue())
                .orElseThrow(() -> new BadRequestException("Requirement not found"));

        if (userOnboardingRepository.existsByUserIdAndRequirementIdAndCompleted(request.userId(),
                request.requirementId(), true)) {
            throw new BadRequestException("User already completed this requirement");
        }

        if (userOnboardingRepository.completeUserOnboarding(request.userId(), request.requirementId()) < 1) {
            throw new BadRequestException("Requirement or user does not exist");
        }

        UserDocument document = UserDocument.builder().url(request.documentUrl()).requirementId(request.requirementId())
                .userId(request.userId()).name(requirements.getRequirementName()).build();
        documentRepository.save(document);

        if (Requirements.of(requirements.getRequirementName()) == Requirements.PROOF_OF_ADDRESS) {
            if (isNull(request.addressRequest())) {
                throw new BadRequestException("Address is required");
            }
            addressRepository.save(Address.builder().city(request.addressRequest().city())
                    .postalCode(request.addressRequest().postalCode())
                    .houseAddress(request.addressRequest().houseAddress())
                            .userId(request.userId())
                    .build());
        }

        if (userOnboardingRepository.allRequirementsSubmitted(request.userId())) {
            usersRepository.completeOnboarding(request.userId());
        }

        return SubmitOnboardingResponse.builder().documentUrl(request.documentUrl())
                .status(true).message(requirements.getRequirementName() + " submission successful.").build();

    }

    /**
     * Retrieves the onboarding details for a user.
     *
     * @param userId the ID of the user
     * @return a list of user onboarding responses
     */
    // TODO: GET FROM SECURITY CONTEXT
    public List<UserOnboardingResponse> getOnboardingDetails(Long userId) {
        return userOnboardingRepository.findAllUserOnboardingsByUserId(userId, Status.ACTIVE.getValue());
    }
}
