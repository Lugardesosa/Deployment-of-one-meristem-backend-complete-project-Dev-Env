package org.meristem.oneapp.usersservice.services;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.meristem.oneapp.usersservice.domains.enums.Status;
import org.meristem.oneapp.usersservice.domains.requests.SubmitOnboardingRequest;
import org.meristem.oneapp.usersservice.domains.responses.SubmitOnboardingResponse;
import org.meristem.oneapp.usersservice.domains.responses.OnboardingResponse;
import org.meristem.oneapp.usersservice.domains.requests.OnboardingRequest;
import org.meristem.oneapp.usersservice.domains.responses.UserOnboardingResponse;
import org.meristem.oneapp.usersservice.dtos.events.UserOnboardingCompletionEvent;
import org.meristem.oneapp.usersservice.exceptionHandler.exceptions.BadRequestException;
import org.meristem.oneapp.usersservice.models.UserDocument;
import org.meristem.oneapp.usersservice.models.UserFeature;
import org.meristem.oneapp.usersservice.models.UserOnboarding;
import org.meristem.oneapp.usersservice.repositories.*;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class OnboardingService {

    private final UserFeatureRepository userFeatureRepository;
    private final UsersRepository usersRepository;
    private final FeatureRepository featureRepository;
    private final FeatureRequirementRepository frRepository;
    private final UserOnboardingRepository userOnboardingRepository;
    private final UserDocumentRepository documentRepository;

    /**
     * Onboards a user to a feature.
     *
     * @param request the onboarding request containing user and feature details
     * @return the response indicating the status of the onboarding process
     * @throws BadRequestException if the feature or user does not exist, or if the user is already onboarded
     */
    @Transactional
    public OnboardingResponse onboard(OnboardingRequest request) {

        if (!featureRepository.existsByIdAndStatus(request.featureId(), Status.ACTIVE.getValue())) {
            throw new BadRequestException("Feature does not exist");
        }

        if (!usersRepository.existsById(request.userId())) {
            throw new BadRequestException("User does not exist");
        }

        if (userFeatureRepository.existsByUserIdAndFeatureId(request.userId(), request.featureId())) {
            throw new BadRequestException("User already onboarded on this Feature");
        }

        // TODO: remove userId from the request and get it from the logged in user data
        userFeatureRepository.save(UserFeature.builder()
            .completed(false).userId(request.userId()).featureId(request.featureId()).build());


        frRepository.findAllByFeatureId(request.featureId())
                .forEach(featureRequirement -> {
            UserOnboarding userOnboarding = UserOnboarding.builder()
                    .completed(false).userId(request.userId()).featureId(featureRequirement.getFeatureId())
                    .requirementId(featureRequirement.getRequirementId()).build();
            userOnboardingRepository.save(userOnboarding);
        });

        return OnboardingResponse.builder().featureId(request.featureId()).status(true).build();
    }


    /**
     * Retrieves the onboarding details for a user and feature.
     *
     * @param userId the ID of the user
     * @param featureId the ID of the feature
     * @return a list of user onboarding responses
     * @throws BadRequestException if the user is not onboarded on the feature
     */
    public List<UserOnboardingResponse> getOnboardingDetails(Long userId, Long featureId) {

        List<UserOnboardingResponse> responses = userOnboardingRepository.findAllUserOnboardingsByUserIdAndFeatureId(userId, featureId);
        if (responses.isEmpty()) {
            throw new BadRequestException("User not onboarded on this Feature");
        }
        return responses;
    }

    /**
     * Completes an onboarding requirement for a user.
     *
     * @param request the onboarding processing request containing user, feature, and requirement details
     * @return the response indicating the status of the onboarding completion process
     * @throws BadRequestException if the requirement is already completed
     */
    // TODO: MAKE ACCESSIBLE ONLY BY LOGGED IN USERS AND USER ID SHOULD BE GOTTEN FROM SECURITY CONTEXT
    @Transactional
    public SubmitOnboardingResponse complete(SubmitOnboardingRequest request) {

        if (userOnboardingRepository.existsByUserIdAndFeatureIdAndRequirementIdAndCompleted(request.userId(),
                request.featureId(), request.requirementId(), true)) {
            throw new BadRequestException("User already completed this requirement");
        }
        userOnboardingRepository.completeUserOnboarding(request.userId(), request.featureId(), request.requirementId());
        UserDocument document = UserDocument.builder().url(request.documentUrl()).requirementId(request.requirementId())
                .userId(request.userId()).name(request.requirementName()).featureId(request.featureId()).build();
        documentRepository.save(document);

        Boolean b = userOnboardingRepository.allRequirementsSubmitted(request.userId(), request.featureId());

        if (userOnboardingRepository.allRequirementsSubmitted(request.userId(), request.featureId())) {
            userFeatureRepository.updateUserFeature(request.userId(), request.featureId());
        }

        return SubmitOnboardingResponse.builder().documentUrl(request.documentUrl()).featureId(request.featureId())
                .status(true).message("This onboarding stage completed").build();
    }
}
