package org.meristem.oneapp.usersservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.meristem.oneapp.usersservice.constants.OkhiEventTypes;
import org.meristem.oneapp.usersservice.domains.enums.*;
import org.meristem.oneapp.usersservice.domains.requests.OkHiWebhookRequest;
import org.meristem.oneapp.usersservice.domains.responses.*;
import org.meristem.oneapp.usersservice.exception.exceptions.BadRequestException;
import org.meristem.oneapp.usersservice.mappers.UsersMapping;
import org.meristem.oneapp.usersservice.models.*;
import org.meristem.oneapp.usersservice.repositories.*;
import org.meristem.oneapp.usersservice.utils.AppUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@Service
@RequiredArgsConstructor
public class OnboardingService {

    private final UsersRepository usersRepository;
    private final UserOnboardingRepository userOnboardingRepository;
    private final AddressRepository addressRepository;
    private final RequirementsRepository requirementsRepository;
    private final UsersService usersService;
    private final GeneralRepository generalRepository;
    private final UsersMapping usersMapping = UsersMapping.INSTANCE;
    private final CustomRepository customRepository;

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
                Users users = usersRepository.findOneByEmail(request.data().metadata().appUserId()).orElseThrow(() -> new BadRequestException("User not found"));
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

                Users users = usersRepository.findOneByEmail(request.data().metadata().appUserId()).orElseThrow(() -> new BadRequestException("User not found"));
                addressRepository.findByUserId(users.getId()).ifPresent(address -> {
                            address.setStatus(AddressStatus.PENDING.getValue());
                            addressRepository.save(address);
                });
            }

            case OkhiEventTypes.ADDRESS_VERIFICATION_COMPLETED -> {

                Requirements requirements = requirementsRepository.findByRequirementNameAndStatus(OnboardingRequirements.PROOF_OF_ADDRESS.getName(), EntityStatus.ACTIVE.getValue());
                Users users = usersRepository.findOneByEmail(request.data().metadata().appUserId()).orElseThrow(() -> new BadRequestException("User not found"));
                if ("verified".equals(request.data().addressVerification().status())) {

                    userOnboardingRepository.updateUserOnboardingStatus(users.getId(), requirements.getId(), OnboardingStatus.APPROVED.getValue(), true);
                    addressRepository.findByUserId(users.getId()).ifPresent(address -> {
                        address.setStatus(AddressStatus.APPROVED.getValue());
                        addressRepository.save(address);
                        usersService.completeUserOnboarding(users.getEmail());
                    });
                } else {

                    userOnboardingRepository.updateUserOnboardingStatus(users.getId(), requirements.getId(), OnboardingStatus.REJECTED.getValue(), false);

                    addressRepository.findByUserId(users.getId()).ifPresent(address -> {
                        address.setStatus(AddressStatus.FAILED.getValue());
                        addressRepository.save(address);
                    });
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

    public Page<CountriesResponse> getCountries() {

        PageRequest pageRequest = getCountryAndStatePageRequest();

        Page<Countries> countries = generalRepository.findAllBy(Countries.class, new HashMap<>(), pageRequest);

        List<CountriesResponse> countriesResponses = usersMapping.countriesToCountriesResponse(countries.getContent());
        return new PageImpl<>(countriesResponses, pageRequest, countries.getTotalElements());
    }

    public Page<StatesResponse> getStates() {

        PageRequest pageRequest = getCountryAndStatePageRequest();
        Countries country = generalRepository.findOneBy(Countries.class, Map.of("code", "NG")).orElseThrow(() -> new BadRequestException("Country not found"));
        Page<CountryStates> countryStates = generalRepository.findAllBy(CountryStates.class, Map.of("countryId", country.getId()), pageRequest);

        List<StatesResponse> statesResponses = usersMapping.countryStatesToStatesResponseResponse(countryStates.getContent());
        return new PageImpl<>(statesResponses, pageRequest, countryStates.getTotalElements());
    }

    private PageRequest getCountryAndStatePageRequest() {

        org.meristem.oneapp.usersservice.domains.requests.PageRequest request = org.meristem.oneapp.usersservice.domains.requests.PageRequest.builder().build();
        int pageSize = 400;
        request.setSortBy(Collections.singletonList("name"));
        request.setSortOrder(Sort.Direction.ASC);
        return PageRequest.of(request.getPage(), pageSize, Sort.by(request.getSortOrder(), String.join(",", request.getSortBy())));
    }

    public List<InstrumentResponse> getInstruments() {

        return customRepository.findAll(InvestmentInstruments.class, (rs, rn) -> InstrumentResponse.builder().id(rs.getLong("id"))
                        .name(rs.getString("name")).code(rs.getString("code")).build());
    }
}
