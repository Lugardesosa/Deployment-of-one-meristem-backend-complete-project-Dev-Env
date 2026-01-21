package org.meristem.oneapp.usersservice.services.implementations;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.meristem.oneapp.usersservice.utils.EncryptionUtil;
import org.meristem.oneapp.usersservice.constants.OkhiEventTypes;
import org.meristem.oneapp.usersservice.domains.enums.*;
import org.meristem.oneapp.usersservice.domains.requests.AddressVerificationRequest;
import org.meristem.oneapp.usersservice.domains.requests.OkHiWebhookRequest;
import org.meristem.oneapp.usersservice.domains.responses.*;
import org.meristem.oneapp.usersservice.exception.exceptions.BadRequestException;
import org.meristem.oneapp.usersservice.mappers.UsersMapping;
import org.meristem.oneapp.usersservice.models.*;
import org.meristem.oneapp.usersservice.repositories.*;
import org.meristem.oneapp.usersservice.services.IOnboardingService;
import org.meristem.oneapp.usersservice.services.IUsersService;
import org.meristem.oneapp.usersservice.utils.AppUtil;
import org.springframework.beans.factory.annotation.Value;
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

import static java.util.Objects.nonNull;


@Slf4j
@Service
@RequiredArgsConstructor
public class OnboardingService implements IOnboardingService {

    private final UsersRepository usersRepository;
    private final UserOnboardingRepository userOnboardingRepository;
    private final AddressRepository addressRepository;
    private final RequirementsRepository requirementsRepository;
    private final IUsersService usersService;
    private final GeneralRepository generalRepository;
    private final UsersMapping usersMapping = UsersMapping.INSTANCE;
    private final CustomRepository customRepository;
    private final HttpServletRequest httpServletRequest;
    private final FilesRepository filesRepository;
    private final IdCardRepository idCardRepository;
    private final EncryptionUtil encryptionUtil;

    @Value("${one-app.users-service.okhi.header-value}")
    private String okhiHeaderId;

    /**
     * Retrieves the onboarding details for a user.
     *
     * @return a list of user onboarding responses
     */
    public List<UserOnboardingResponse> getOnboardingDetails() {
        return userOnboardingRepository.findAllUserOnboardingsByUserId(AppUtil.getLoggedInUserId(), EntityStatus.ACTIVE.getValue(), RequirementType.DEFAULT.getId());
    }

    /**
     * Handles OkHi webhook callbacks and updates the user's address and onboarding status based on the event type.
     * Supported events include address collection, verification started, verification completed, and verification cancelled.
     *
     * @param request the webhook payload received from OkHi
     * @return a response indicating the processing outcome
     * @throws BadRequestException if the event type is unknown or the referenced user cannot be found
     */
    @Transactional
    public OkHiWebhookResponse handleOkhiWebhook(OkHiWebhookRequest request) {

        if (nonNull(httpServletRequest.getHeader("X-MERISTEM-KEY")) && !httpServletRequest.getHeader("X-MERISTEM-KEY").equals(okhiHeaderId)) {
            throw new BadRequestException("Invalid API key");
        }

        switch (request.eventType()) {

            case OkhiEventTypes.ADDRESS_COLLECTED -> {
                Users users = usersRepository.findOneByEmail(request.data().metadata().appUserId()).orElseThrow(() -> new BadRequestException("User not found"));
                addressRepository.findByUserIdAndVerificationMethod(users.getId(), AddressVerificationMethod.AUTO.getValue()).ifPresentOrElse(address -> {
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
                                    .verificationMethod(AddressVerificationMethod.AUTO.getValue())
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

    /**
     * Retrieves a paginated list of supported countries, sorted by name in ascending order.
     *
     * @return a page of country responses
     */
    public Page<CountriesResponse> getCountries() {

        PageRequest pageRequest = getCountryAndStatePageRequest();

        Page<Countries> countries = generalRepository.findAllBy(Countries.class, new HashMap<>(), pageRequest);

        List<CountriesResponse> countriesResponses = usersMapping.countriesToCountriesResponse(countries.getContent());
        return new PageImpl<>(countriesResponses, pageRequest, countries.getTotalElements());
    }

    /**
     * Retrieves a paginated list of supported states for the default country, sorted by name in ascending order.
     *
     * @return a page of state/province responses
     * @throws BadRequestException if the default country cannot be found
     */
    public Page<StatesResponse> getStates() {

        PageRequest pageRequest = getCountryAndStatePageRequest();
        Countries country = generalRepository.findOneBy(Countries.class, Map.of("code", "NG")).orElseThrow(() -> new BadRequestException("Country not found"));
        Page<CountryStates> countryStates = generalRepository.findAllBy(CountryStates.class, Map.of("countryId", country.getId()), pageRequest);

        List<StatesResponse> statesResponses = usersMapping.countryStatesToStatesResponseResponse(countryStates.getContent());
        return new PageImpl<>(statesResponses, pageRequest, countryStates.getTotalElements());
    }

    /**
     * Builds a PageRequest for country and state lookups with a fixed page size and name-based ascending sort.
     *
     * @return a configured PageRequest for country/state queries
     */
    private PageRequest getCountryAndStatePageRequest() {

        org.meristem.oneapp.usersservice.domains.requests.PageRequest request = org.meristem.oneapp.usersservice.domains.requests.PageRequest.builder().build();
        int pageSize = 400;
        request.setSortBy(Collections.singletonList("name"));
        request.setSortOrder(Sort.Direction.ASC);
        return PageRequest.of(request.getPage(), pageSize, Sort.by(request.getSortOrder(), String.join(",", request.getSortBy())));
    }

    /**
     * Retrieves all available investment instruments.
     *
     * @return a list of instrument responses
     */
    public List<InstrumentResponse> getInstruments() {

        return customRepository.findAll(InvestmentInstruments.class, (rs, rn) -> InstrumentResponse.builder().id(rs.getLong("id"))
                .name(rs.getString("name")).code(rs.getString("code")).build());
    }

    public AddressVerificationResponse submitAddress(AddressVerificationRequest request) {

        filesRepository.save(Files.builder().userId(AppUtil.getLoggedInUserId()).fileKey(request.fileKey()).contentType(request.contentType()).fileType(FileType.DOCUMENT.getValue()).build());

        addressRepository.findByUserIdAndVerificationMethod(AppUtil.getLoggedInUserId(), AddressVerificationMethod.MANUAL.getValue()).ifPresentOrElse(address -> {

            address.setCity(request.city());
            address.setState(request.state());
            address.setHouseAddress(request.houseAddress());
            address.setLandmark(request.landMark());
            address.setUtilityBillType(request.utilityBillType().getValue());
            address.setDocumentKey(request.fileKey());
            address.setStatus(AddressStatus.PENDING.getValue());
            addressRepository.save(address);
        }, () -> addressRepository.save(Address.builder()
                .verificationMethod(AddressVerificationMethod.MANUAL.getValue())
                .houseAddress(request.houseAddress())
                .userId(AppUtil.getLoggedInUserId())
                .state(request.state())
                .city(request.city())
                .landmark(request.landMark())
                .utilityBillType(request.utilityBillType().getValue())
                .status(AddressStatus.PENDING.getValue())
                .documentKey(request.fileKey())
                .build()));
        return AddressVerificationResponse.builder().message("Successful").status(true).build();
    }

    public GetIdNumberResponse getIdNumber(String idType) {
        String idNumber = idCardRepository.findIdCardValueByUserId(AppUtil.getLoggedInUserId(), idType);
        return new GetIdNumberResponse(encryptionUtil.decrypt(idNumber));
    }
}
