package org.meristem.oneapp.usersservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.meristem.oneapp.kafka.dtos.KycCompletedDto;
import org.meristem.oneapp.usersservice.constants.AppConstants;
import org.meristem.oneapp.usersservice.constants.KafkaTopics;
import org.meristem.oneapp.usersservice.domains.enums.*;
import org.meristem.oneapp.usersservice.constants.OkhiEventTypes;
import org.meristem.oneapp.usersservice.domains.requests.AddressOnboardRequest;
import org.meristem.oneapp.usersservice.domains.requests.OkHiWebhookRequest;
import org.meristem.oneapp.usersservice.domains.responses.AddressOnboardingResponse;
import org.meristem.oneapp.usersservice.domains.responses.OkHiWebhookResponse;
import org.meristem.oneapp.usersservice.domains.responses.UserOnboardingResponse;
import org.meristem.oneapp.usersservice.exception.exceptions.BadRequestException;
import org.meristem.oneapp.usersservice.models.*;
import org.meristem.oneapp.usersservice.repositories.*;
import org.meristem.oneapp.usersservice.utils.AppUtil;
import org.springframework.cache.CacheManager;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static java.util.Objects.requireNonNull;

@Slf4j
@Service
@RequiredArgsConstructor
public class OnboardingService {

    private final UsersRepository usersRepository;
    private final UserOnboardingRepository userOnboardingRepository;
    private final AddressRepository addressRepository;
    private final UserProfileRepository userProfileRepository;
    private final CacheManager cacheManager;
    private final RequirementsRepository requirementsRepository;
    private final KafkaSenderService kafkaSenderService;

    /**
     * Retrieves the onboarding details for a user.
     *
     * @return a list of user onboarding responses
     */
    public List<UserOnboardingResponse> getOnboardingDetails() {
        return userOnboardingRepository.findAllUserOnboardingsByUserId(AppUtil.getLoggedInUserId(), EntityStatus.ACTIVE.getValue(), RequirementType.DEFAULT.getId());
    }


    @Transactional
    public OkHiWebhookResponse handleOkhiWebhook(OkHiWebhookRequest request) {
        switch (request.eventType()) {

            case OkhiEventTypes.ADDRESS_COLLECTED -> {
                Users users = usersRepository.findOneByEmail(request.data().metadata().appUserId());
                addressRepository.findByUserId(users.getId()).ifPresentOrElse(address -> {
                        address.setCity(request.data().location().city());
                        address.setCountry(request.data().location().country());
                        address.setState(request.data().location().state());
                        address.setHouseAddress(request.data().location().formattedAddress());
                        address.setNumber(request.data().location().propertyNumber());
                        address.setStatus(AddressStatus.PENDING.getValue());
                        addressRepository.save(address);
                    },
                    () -> {
                        Address address = Address.builder()
                            .userId(users.getId())
                            .street(request.data().location().streetName())
                            .city(request.data().location().city())
                            .country(request.data().location().country())
                            .state(request.data().location().state())
                            .number(request.data().location().propertyNumber())
                            .status(AddressStatus.PENDING.getValue())
                            .houseAddress(request.data().location().formattedAddress())
                            .build();
                        addressRepository.save(address);
                    });
            }

            case OkhiEventTypes.ADDRESS_VERIFICATION_STARTED -> {

                Users users = usersRepository.findOneByEmail(request.data().metadata().appUserId());
                addressRepository.findByUserId(users.getId()).ifPresent(address -> {
                            address.setStatus(AddressStatus.PENDING.getValue());
                            addressRepository.save(address);
                });
            }

            case OkhiEventTypes.ADDRESS_VERIFICATION_COMPLETED -> {

                Requirements requirements = requirementsRepository.findByRequirementNameAndStatus(OnboardingRequirements.PROOF_OF_ADDRESS.getName(), EntityStatus.ACTIVE.getValue());
                Users users = usersRepository.findOneByEmail(request.data().metadata().appUserId());

                Long userId = users.getId();
                userOnboardingRepository.updateUserOnboardingStatus(users.getId(), requirements.getId(), OnboardingStatus.APPROVED.getValue(), true);
                addressRepository.findByUserId(users.getId()).ifPresent(address -> {
                    address.setStatus(AddressStatus.APPROVED.getValue());
                    addressRepository.save(address);
                });

                if (userOnboardingRepository.allRequirementsSubmitted(userId)) {
                    userProfileRepository.completeOnboarding(userId);
                    requireNonNull(cacheManager.getCache(AppConstants.USERS_CACHE_NAME)).evict(AppUtil.getLoggedInUserEmail());
                    kafkaSenderService.send(KycCompletedDto.builder().userId(userId).fullName(AppUtil.getUserFullName(users)).build(), Map.of(KafkaHeaders.TOPIC, KafkaTopics.KAFKA_KYC_COMPLETED));
                }
            }

            case OkhiEventTypes.ADDRESS_VERIFICATION_CANCELLED -> {

                Requirements requirements = requirementsRepository.findByRequirementNameAndStatus(OnboardingRequirements.PROOF_OF_ADDRESS.getName(), EntityStatus.ACTIVE.getValue());
                Long userId = usersRepository.findIdByEmail(request.data().metadata().appUserId());

                userOnboardingRepository.updateUserOnboardingStatus(userId, requirements.getId(), OnboardingStatus.REJECTED.getValue(), false);

                addressRepository.findByUserId(userId).ifPresent(address -> {
                    address.setStatus(AddressStatus.CANCELLED.getValue());
                    addressRepository.save(address);
                });
            }
            default -> throw new BadRequestException("Unknown event type");
        }
        return OkHiWebhookResponse.builder().message("Success").success(true).build();
    }
}
