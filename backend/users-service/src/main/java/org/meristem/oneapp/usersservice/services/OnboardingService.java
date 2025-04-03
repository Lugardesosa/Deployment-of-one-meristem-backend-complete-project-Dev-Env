package org.meristem.oneapp.usersservice.services;


import lombok.RequiredArgsConstructor;
import org.meristem.oneapp.usersservice.domains.responses.OnboardingResponse;
import org.meristem.oneapp.usersservice.domains.requests.OnboardingRequest;
import org.meristem.oneapp.usersservice.domains.responses.UserOnboardingResponse;
import org.meristem.oneapp.usersservice.exceptionHandler.exceptions.BadRequestException;
import org.meristem.oneapp.usersservice.models.UserFeature;
import org.meristem.oneapp.usersservice.models.UserOnboarding;
import org.meristem.oneapp.usersservice.repositories.*;
import org.springframework.data.relational.core.conversion.DbActionExecutionException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OnboardingService {

    private final UserFeatureRepository userFeatureRepository;
    private final UsersRepository usersRepository;
    private final FeatureRepository featureRepository;
    private final FeatureRequirementRepository frRepository;
    private final UserOnboardingRepository userOnboardingRepository;

    @Transactional
    public OnboardingResponse onboard(OnboardingRequest request) {

        if (!featureRepository.existsById(request.featureId())) {
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


    public List<UserOnboardingResponse> getOnboardingDetails(Long userId, Long featureId) {

        return userOnboardingRepository.findALlUserOnboardingsByUserIdAndFeatureId(userId, featureId);
    }
}
