package org.meristem.oneapp.usersservice.services.implementations;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.obs.services.model.HttpMethodEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.meristem.oneapp.kafka.dtos.*;
import org.meristem.oneapp.usersservice.constants.AppConstants;
import org.meristem.oneapp.usersservice.constants.KafkaTopics;
import org.meristem.oneapp.usersservice.constants.MessageSubjects;
import org.meristem.oneapp.usersservice.domains.enums.*;
import org.meristem.oneapp.usersservice.domains.requests.*;
import org.meristem.oneapp.usersservice.domains.responses.*;
import org.meristem.oneapp.usersservice.dtos.CreateJointAccountDtos;
import org.meristem.oneapp.usersservice.dtos.IdQueryDetailsDto;
import org.meristem.oneapp.usersservice.dtos.OtpVerificationDto;
import org.meristem.oneapp.usersservice.exception.exceptions.BadRequestException;
import org.meristem.oneapp.usersservice.exception.exceptions.ContextException;
import org.meristem.oneapp.usersservice.exception.exceptions.ResourceNotFoundException;
import org.meristem.oneapp.usersservice.integrations.MiddleWareClient;
import org.meristem.oneapp.usersservice.integrations.requests.CreateIndividualCustomerRequest;
import org.meristem.oneapp.usersservice.integrations.requests.UpdateAddressRequest;
import org.meristem.oneapp.usersservice.integrations.responses.CreateIndividualCustomerResponse;
import org.meristem.oneapp.usersservice.integrations.responses.MiddlewareBaseApiResponse;
import org.meristem.oneapp.usersservice.integrations.responses.MiddlewareCustomerResponse;
import org.meristem.oneapp.usersservice.integrations.responses.MiddlewareResponse;
import org.meristem.oneapp.usersservice.mappers.MiddlewareMapper;
import org.meristem.oneapp.usersservice.mappers.UsersMapping;
import org.meristem.oneapp.usersservice.models.*;
import org.meristem.oneapp.usersservice.repositories.*;
import org.meristem.oneapp.usersservice.services.IKafkaSenderService;
import org.meristem.oneapp.usersservice.services.IUsersService;
import org.meristem.oneapp.usersservice.utils.AppUtil;
import org.meristem.oneapp.usersservice.utils.EncryptionUtil;
import org.meristem.oneapp.usersservice.utils.HashingUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Objects.*;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Service class for managing user-related operations.
 * Provides methods for creating users, updating user details, and handling user authentication and profile updates.
 *
 * @author Kingsley
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UsersService implements IUsersService {

    private final UsersRepository usersRepository;
    private final UsersMapping usersMapper = UsersMapping.INSTANCE;
    private final MiddlewareMapper middlewareMapper = MiddlewareMapper.INSTANCE;
    private final PasswordEncoder passwordEncoder;
    private final IKafkaSenderService kafkaSenderService;
    private final UserProfileRepository userProfileRepository;
    private final FilesRepository filesRepository;
    private final CacheManager cacheManager;
    private final UserProfileRepository profileRepository;
    private final RequirementsRepository requirementsRepository;
    private final UserOnboardingRepository userOnboardingRepository;
    private final RolesRepository rolesRepository;
    private final HuaweiService huaweiService;
    private final GeneralRepository generalRepository;
    private final CustomRepository customRepository;
    private final IdCardRepository idCardRepository;
    private final UserPinRepository userPinRepository;
    private final HashingUtil hashingUtil;
    private final OutboxEventRepository outboxEventRepository;
    private final ObjectMapper objectMapper;
    private final MiddleWareClient middleWareClient;
    private final CountriesRepositories countriesRepositories;
    private final IdDetailsService idDetailsService;
    private final OtpService otpService;
    private final EncryptionUtil encryptionUtil;
    private final JointAccountRepository jointAccountRepository;
    private final InvestmentInstrumentsRepository investmentInstrumentsRepository;

    @Value("${hashing.id-hash-key}")
    private String idHashKey;

    public UpdateResponse create(CreateUserRequest request) {

        checkEmailOrPhoneDoesNotExist(request.email(), request.phoneNumber());
        Cache cache = requireNonNull(cacheManager.getCache(AppConstants.SIGN_UP_CACHE_NAME));

        IdQueryDetailsDto bvnQueryResponse = cache.get(hashingUtil.hmacWithSha256(idHashKey, request.bvn()), IdQueryDetailsDto.class);

        if (bvnQueryResponse == null) {
            throw new ResourceNotFoundException("Initial sign up details not found.", "Bvn", request.bvn().substring(0, 3) + "*****" + request.bvn().substring(8, 11));
        }

        if (idCardRepository.existsByIdValueHashedAndIdCardType(request.bvn(), IdCardType.BVN.getName())) {
            throw new BadRequestException("Bvn already exists.");
        }

        cache.evict(bvnQueryResponse.getBvnHashed());
        bvnQueryResponse.setEmail(request.email());
        bvnQueryResponse.setPhoneNumber(request.phoneNumber());
        bvnQueryResponse.setOccupation(request.occupation());
        bvnQueryResponse.setSourceOfIncome(request.sourceOfIncome());
        bvnQueryResponse.setEmployerName(request.employerName());
        cache.put(request.email(), bvnQueryResponse);

        log.info("First stage of User with email {} created ", request.email());
        return UpdateResponse.builder().success(true).message("Successful").build();
    }

    @Override
    public UpdateResponse createJoint(CreateJointUserRequest request) {

        checkEmailOrPhoneDoesNotExist(request.primary().email(), request.primary().phoneNumber());
        checkEmailOrPhoneDoesNotExist(request.secondary().email(), request.secondary().phoneNumber());

        Cache cache = requireNonNull(cacheManager.getCache(AppConstants.SIGN_UP_CACHE_NAME));

        IdQueryDetailsDto primary = cache.get(hashingUtil.hmacWithSha256(idHashKey, request.primary().bvn()), IdQueryDetailsDto.class);
        IdQueryDetailsDto secondary = cache.get(hashingUtil.hmacWithSha256(idHashKey, request.secondary().bvn()), IdQueryDetailsDto.class);

        if (primary == null) {
            throw new ResourceNotFoundException("Initial sign up details not found.", "Bvn", request.primary().bvn().substring(0, 3) + "*****" + request.primary().bvn().substring(8, 11));
        }
        if (secondary == null) {
            throw new ResourceNotFoundException("Initial sign up details not found.", "Bvn", request.secondary().bvn().substring(0, 3) + "*****" + request.secondary().bvn().substring(8, 11));
        }

        if (idCardRepository.existsByIdValueHashedAndIdCardType(request.primary().bvn(), IdCardType.BVN.getName())) {
            throw new BadRequestException("Bvn already exists " + request.primary().bvn().substring(0, 3) + "*****" + request.primary().bvn().substring(8, 11));
        }
        if (idCardRepository.existsByIdValueHashedAndIdCardType(request.secondary().bvn(), IdCardType.BVN.getName())) {
            throw new BadRequestException("Bvn already exists " + request.secondary().bvn().substring(0, 3) + "*****" + request.secondary().bvn().substring(8, 11));
        }

        cache.evict(primary.getBvnHashed());
        primary.setEmail(request.primary().email());
        primary.setPhoneNumber(request.primary().phoneNumber());
        primary.setOccupation(request.primary().occupation());
        primary.setSourceOfIncome(request.primary().sourceOfIncome());
        primary.setEmployerName(request.primary().employerName());

        cache.evict(secondary.getBvnHashed());
        secondary.setEmail(request.secondary().email());
        secondary.setPhoneNumber(request.secondary().phoneNumber());
        secondary.setOccupation(request.secondary().occupation());
        secondary.setSourceOfIncome(request.secondary().sourceOfIncome());
        secondary.setEmployerName(request.secondary().employerName());

        requireNonNull(cacheManager.getCache(AppConstants.JOINT_SIGN_UP_CACHE_NAME)).put(request.primary().email(),
                CreateJointAccountDtos.builder().accountName(request.accountName()).mandateType(request.mandateType())
                        .operationType(request.operationType()).primary(primary).secondary(secondary).build());

        log.info("First stage of joint User with email {} created ", request.primary().email());
        return UpdateResponse.builder().success(true).message("Successful").build();
    }

    @Transactional
    @Override
    public UpdateResponse verifyEmail(VerifyOtpRequest request) {

        List<Integer> allowedOtpTypes = List.of(MessageSubject.EMAIL_VERIFICATION.getCode(), MessageSubject.JOINT_EMAIL_VERIFICATION.getCode(), MessageSubject.SECONDARY_EMAIL_VERIFICATION.getCode(), MessageSubject.EXISTING_EMAIL_VERIFICATION.getCode());
        if (!allowedOtpTypes.contains(request.otpType())) {
            throw new BadRequestException("Only email can be verified");
        }
        VerifyOtpResponse response = otpService.verifyOtp(request);

        if (response.status()) {
            if (request.otpType().equals(MessageSubject.EMAIL_VERIFICATION.getCode())) {
                validateAndMarkEmailAsVerified(request.recipient());
            } else if (request.otpType().equals(MessageSubject.JOINT_EMAIL_VERIFICATION.getCode())) {
                validateAndMarkEmailAsVerifiedJoint(request.recipient());
            } else if (request.otpType().equals(MessageSubject.SECONDARY_EMAIL_VERIFICATION.getCode())) {
                Long userId = usersRepository.findIdByEmailOrPhoneNumber(request.recipient(), request.recipient());
                userProfileRepository.updateEmailVerified(userId, true);
            } else if (request.otpType().equals(MessageSubject.EXISTING_EMAIL_VERIFICATION.getCode())) {
                validateAndMarkEmailAsVerifiedExisting(request.recipient());
            } else {
                throw new BadRequestException("Only one of these codes are allowed " +
                        allowedOtpTypes);
            }
            return UpdateResponse.builder().success(true).message("Email verified").build();
        } else {
            return UpdateResponse.builder().success(false).message("OTP not verified").build();
        }
    }

    @Transactional
    @Override
    public UpdateResponse setPassword(SetPasswordRequest userRequest) {

        log.info("User with email {} started stage 2", userRequest.email());
        Cache cache = requireNonNull(cacheManager.getCache(AppConstants.SIGN_UP_CACHE_NAME));
        IdQueryDetailsDto bvnQueryResponse = cache.get(userRequest.email(), IdQueryDetailsDto.class);

        if (bvnQueryResponse == null) {
            throw new ResourceNotFoundException("Initial sign up details not found.", "Email", userRequest.email());
        }

        if (!bvnQueryResponse.isEmailVerified()) {
            throw new BadRequestException("Email not verified.");
        }
        Users user = usersMapper.ninQueryResponseToUsers(bvnQueryResponse);

        user.setPassword(passwordEncoder.encode(userRequest.password()));
        user.setAccountType(AccountType.INDIVIDUAL.getValue());
        UserProfile profile = save(user, bvnQueryResponse, true);
        saveToOutbox(user, bvnQueryResponse, profile, user.getId());
        cache.evict(userRequest.email());
        log.info("User with email {} completed stage 2 of onboarding process", userRequest.email());
        return UpdateResponse.builder().success(true).message("Password successfully set.").build();
    }

    @Transactional
    @Override
    public UpdateResponse setJointPassword(SetPasswordRequest userRequest) {

        log.info("Joint User with email {} started stage 2", userRequest.email());
        Cache cache = requireNonNull(cacheManager.getCache(AppConstants.JOINT_SIGN_UP_CACHE_NAME));
        CreateJointAccountDtos bvnQueryResponse = cache.get(userRequest.email(), CreateJointAccountDtos.class);

        if (bvnQueryResponse == null) {
            throw new ResourceNotFoundException("Initial sign up details not found.", "Email", userRequest.email());
        }

        if (!bvnQueryResponse.getPrimary().isEmailVerified()) {
            throw new BadRequestException("Email not verified.");
        }
        Users primary = usersMapper.ninQueryResponseToUsers(bvnQueryResponse.getPrimary());
        Users secondary = usersMapper.ninQueryResponseToUsers(bvnQueryResponse.getSecondary());

        primary.setPassword(passwordEncoder.encode(userRequest.password()));
        primary.setAccountType(AccountType.JOINT.getValue());

        secondary.setAccountType(AccountType.JOINT.getValue());
        UserProfile primaryProfile = save(primary, bvnQueryResponse.getPrimary(), true);
        UserProfile secondaryProfile = save(secondary, bvnQueryResponse.getSecondary(), false);

        String accountId = UUID.randomUUID().toString();
        JointAccount jointAccountPrimary = JointAccount.builder()
                .accountName(bvnQueryResponse.getAccountName())
                .userId(primary.getId())
                .accountId(accountId)
                .role(JointAccountType.PRIMARY.getValue())
                .mandateType(bvnQueryResponse.getMandateType().getValue())
                .operationMode(bvnQueryResponse.getOperationType().getValue())
                .build();

        JointAccount jointAccountSecondary = JointAccount.builder()
                .accountName(bvnQueryResponse.getAccountName())
                .userId(secondary.getId())
                .accountId(accountId)
                .role(JointAccountType.SECONDARY.getValue())
                .mandateType(bvnQueryResponse.getMandateType().getValue())
                .operationMode(bvnQueryResponse.getOperationType().getValue())
                .build();

        jointAccountRepository.save(jointAccountPrimary);
        jointAccountRepository.save(jointAccountSecondary);

        saveJointToOutbox(primary, secondary, bvnQueryResponse, primaryProfile, secondaryProfile, accountId);

        cache.evict(userRequest.email());
        log.info("Joint User with email {} completed stage 2 of onboarding process", userRequest.email());
        return UpdateResponse.builder().success(true).message("Password successfully set.").build();
    }

    @Transactional
    @Override
    public UpdateResponse setPasswordExisting(SetPasswordRequest request) {
        log.info("User with existing email {} started stage 2", request.email());
        Cache cache = requireNonNull(cacheManager.getCache(AppConstants.EXISTING_USER_SIGN_UP_CACHE_NAME));
        MiddlewareCustomerResponse.CustomerData bvnQueryResponse = cache.get(request.email(), MiddlewareCustomerResponse.CustomerData.class);

        if (bvnQueryResponse == null) {
            throw new ResourceNotFoundException("Initial sign up details not found.", "Email", request.email());
        }

        if (!bvnQueryResponse.getEmailVerified()) {
            throw new BadRequestException("Email not verified.");
        }
        Users user = usersMapper.coreBvnQueryResponseToUser(bvnQueryResponse);

        user.setPassword(passwordEncoder.encode(request.password()));
        user.setAccountType(AccountType.fromString(bvnQueryResponse.getCustomerType()).getValue());
        saveExisting(user, bvnQueryResponse, true);
        cache.evict(request.email());
        log.info("User with existing email {} completed stage 2 of onboarding process", request.email());
        return UpdateResponse.builder().success(true).message("Password successfully set.").build();
    }

    @Override
    public UpdateResponse setJointPasswordSecondary(SetPasswordRequest request) {
        Users user = usersRepository.findUsersByEmail(request.email());

        if (!userProfileRepository.findEmailVerifiedByUserId(user.getId())) {
            throw new BadRequestException("Email not verified.");
        }
        user.setPassword(passwordEncoder.encode(request.password()));
        usersRepository.save(user);
        return UpdateResponse.builder().success(true).message("Successful").build();
    }

    // TODO: DO BVN ID QUERY ON KYC AND SAVE TO USER ID TABLE
    private void saveExisting(Users user, MiddlewareCustomerResponse.CustomerData bvnQueryResponse, boolean emailVerified) {

        log.info("User with existing email {} onboarding completion started ", user.getEmail());

        user.setStatus(UserStatus.DATA_SHARING_NOT_COMPLETED.getValue());
        user = usersRepository.save(user);

        idCardRepository.save(IdCard.builder().idValue(encryptionUtil.encrypt(bvnQueryResponse.getBankBvn()))
                .idCardType(IdCardType.BVN.getName())
                .userId(user.getId()).idValueHashed(hashingUtil.hmacWithSha256(idHashKey, bvnQueryResponse.getBankBvn()))
                .build());

        UserProfile profile = configureUserOnboarding(user, bvnQueryResponse.getGenderCode(), emailVerified);

        log.info("User with existing email {} onboarding completion finished ", user.getEmail());
    }

    private @NonNull UserProfile configureUserOnboarding(Users user, String gender, boolean emailVerified) {
        String referralCode;
        do {
            referralCode = AppUtil.generateReferralCode(user.getFirstName());
        } while (userProfileRepository.existsByReferralCode(referralCode));
        UserProfile profile = UserProfile.builder().userId(user.getId()).gender(Gender.getGender(gender).getCaps()).referralCode(referralCode).build();
        profile.setEmailVerified(emailVerified);

        profileRepository.save(profile);
        Long userId = user.getId();
        requirementsRepository.findAllByStatus(EntityStatus.ACTIVE.getValue())
                .forEach(rId -> {
                    UserOnboarding userOnboarding = UserOnboarding.builder().status(OnboardingStatus.NOT_STARTED.getValue())
                            .completed(false).userId(userId).requirementId(rId).build();
                    userOnboardingRepository.save(userOnboarding);
                });
        customRepository.saveAll(customRepository.findAll(InvestmentInstruments.class)
                .stream().map(i -> UserInstrument.builder().userId(userId).instrumentId(i.getId()).build()).toList());
        customRepository.saveAll(customRepository.findAll(InvestmentOptions.class)
                .stream().map(i -> InvestmentOptionsAccessed.builder().userId(userId).optionId(i.getId()).build()).toList());
        usersRepository.saveRole(userId, rolesRepository.findIdByName(AppConstants.USER_ROLE));
        return profile;
    }

    @Override
    public List<JointAccountDetailsResponse> getJointAccountDetails() {

        if (Long.valueOf(AccountType.INDIVIDUAL.getValue()).equals(AppUtil.getLoggedInUserAccountType())) {
            return List.of();
        }
        return jointAccountRepository.findAccountPartiesByUserId(AppUtil.getLoggedInUserId());
    }

    // TODO
    @Override
    public UsersResponse getInvestmentInstrument() {
        return investmentInstrumentsRepository.findUserInstrumentsById(AppUtil.getLoggedInUserId());
    }

    @Override
    public UpdateResponse existingCustomer(ExistingCustomerRequest request) {

        MiddlewareCustomerResponse response = middleWareClient.getCustomerByBvn(request.idNumber()).data();
        if (!response.data().isEmpty()) {
            response.data().stream().findFirst().ifPresent(r -> {
                // TODO: Reconcile existing account on core with one on this platform
                if (usersRepository.existsByEmailOrPhoneNumber(r.getEmailAddress(), r.getPhoneNumbers())) {
                    throw new BadRequestException("Email or Phone number already exists.");
                }
                Cache cache = requireNonNull(cacheManager.getCache(AppConstants.EXISTING_USER_SIGN_UP_CACHE_NAME));
                cache.put(r.getEmailAddress(), r);
                if (org.apache.commons.lang3.StringUtils.isNotBlank(r.getBankBvn())) {
                    otpService.sendOtp(SendOtpRequest.builder().otpType(MessageSubject.EXISTING_EMAIL_VERIFICATION.getCode()).recipient(r.getEmailAddress()).messageMedium(MessageMedium.EMAIL.getValue()).build());
                }
            });
        }
        return UpdateResponse.builder().success(true).message("If customer with the bvn exists, you will receive an otp in the email linked to it").build();
    }

    public UserProfile save(Users user, IdQueryDetailsDto bvnQueryResponse, Boolean emailVerified) {
        log.info("User with email {} onboarding completion started ", user.getEmail());
        if (usersRepository.existsByEmailOrPhoneNumber(user.getEmail(), user.getPhoneNumber())) {
            throw new BadRequestException("Email or Phone number already exists.");
        }

        if (idCardRepository.existsByIdValueHashedAndIdCardType(bvnQueryResponse.getBvnHashed(), IdCardType.BVN.getName())) {
            throw new BadRequestException("BVN already exists.");
        }

        user.setStatus(UserStatus.DATA_SHARING_NOT_COMPLETED.getValue());
        user = usersRepository.save(user);

        idCardRepository.save(IdCard.builder().idValue(bvnQueryResponse.getBvn())
                .idCardType(IdCardType.BVN.getName())
                .userId(user.getId()).idValueHashed(bvnQueryResponse.getBvnHashed())
                .build());

        log.info("User with email {} onboarding completion finished ", user.getEmail());
        return configureUserOnboarding(user, bvnQueryResponse.getGender(), emailVerified);
    }

    private void saveToOutbox(Users user, IdQueryDetailsDto bvnQueryResponse, UserProfile profile, Long userId) {
        String address, city, countryCode;

        Optional<Countries> countries = countriesRepositories.findCountriesByCodeLongOrCodeShortOrNameIgnoreCase(bvnQueryResponse.getNationality(), bvnQueryResponse.getNationality(), bvnQueryResponse.getNationality());
        if (nonNull(bvnQueryResponse.getCountry()) && nonNull(bvnQueryResponse.getAddress()) && countries.isPresent()) {

            String[] addressSplit = bvnQueryResponse.getAddress().split(",");
            address = bvnQueryResponse.getAddress();
            city = isBlank(bvnQueryResponse.getLocalAreaOfOrigin()) ? (addressSplit.length > 0 ? addressSplit[addressSplit.length - 1] : "TEMPORARY") : bvnQueryResponse.getLocalAreaOfOrigin();
            countryCode = countries.get().getCodeLong();
        } else {
            address = "TEMPORARY";
            city = "TEMPORARY";
            countryCode = "NGA";
        }

        CreateCustomerDto createCustomerDto = CreateCustomerDto.builder()
                .email(user.getEmail()).gender(profile.getGender())
                .middleName(user.getMiddleName())
                .lastName(user.getLastName()).userId(userId)
                .firstName(user.getFirstName())
                .phoneNumber(user.getPhoneNumber())
                .addressStreet(address).addressCity(city)
                .addressCountryCd(countryCode).build();

        try {
            OutboxEvent customer = OutboxEvent.builder()
                    .aggregateId(user.getId()).aggregateType(AggregateType.USER.getValue())
                    .eventType(KafkaTopics.KAFKA_CUSTOMER_CREATE_TOPIC)
                    .outboxStatus(OutboxStatus.PENDING.getValue())
                    .eventClass(CreateCustomerDto.class.getName())
                    .eventKey(user.getId().toString())
                    .payload(objectMapper.writeValueAsString(createCustomerDto)).build();
            outboxEventRepository.save(customer);

            idDetailsService.buildAndSaveIdDetails(bvnQueryResponse, user);

        } catch (JsonProcessingException e) {
            log.error("Error creating customer for user with id {} to outbox", user.getId(), e);
        }
    }

    private void saveJointToOutbox(Users primary, Users secondary, CreateJointAccountDtos jointAccountDtos, UserProfile primaryProfile, UserProfile secondaryProfile, String accountId) {
        String address1, city1, countryCode1;

        Optional<Countries> countries = countriesRepositories.findCountriesByCodeLongOrCodeShortOrNameIgnoreCase(jointAccountDtos.getPrimary().getNationality(), jointAccountDtos.getPrimary().getNationality(), jointAccountDtos.getPrimary().getNationality());
        if (nonNull(jointAccountDtos.getPrimary().getCountry()) && nonNull(jointAccountDtos.getPrimary().getAddress()) && countries.isPresent()) {

            String[] addressSplit = jointAccountDtos.getPrimary().getAddress().split(",");
            address1 = jointAccountDtos.getPrimary().getAddress();
            city1 = isBlank(jointAccountDtos.getPrimary().getLocalAreaOfOrigin()) ? (addressSplit.length > 0 ? addressSplit[addressSplit.length - 1] : "TEMPORARY") : jointAccountDtos.getPrimary().getLocalAreaOfOrigin();
            countryCode1 = countries.get().getCodeLong();
        } else {
            address1 = "TEMPORARY";
            city1 = "TEMPORARY";
            countryCode1 = "NGA";
        }

        String address2, city2, countryCode2;
        Optional<Countries> countries2 = countriesRepositories.findCountriesByCodeLongOrCodeShortOrNameIgnoreCase(jointAccountDtos.getSecondary().getNationality(), jointAccountDtos.getSecondary().getNationality(), jointAccountDtos.getSecondary().getNationality());
        if (nonNull(jointAccountDtos.getSecondary().getCountry()) && nonNull(jointAccountDtos.getSecondary().getAddress()) && countries2.isPresent()) {

            String[] addressSplit2 = jointAccountDtos.getSecondary().getAddress().split(",");
            address2 = jointAccountDtos.getSecondary().getAddress();
            city2 = isBlank(jointAccountDtos.getSecondary().getLocalAreaOfOrigin()) ? (addressSplit2.length > 0 ? addressSplit2[addressSplit2.length - 1] : "TEMPORARY") : jointAccountDtos.getSecondary().getLocalAreaOfOrigin();
            countryCode2 = countries2.get().getCodeLong();
        } else {
            address2 = "TEMPORARY";
            city2 = "TEMPORARY";
            countryCode2 = "NGA";
        }

        CreateJointCustomerDto createCustomerDto = CreateJointCustomerDto.builder()
                .accountName(jointAccountDtos.getAccountName())

                .person1FirstName(primary.getFirstName())
                .person1LastName(primary.getLastName())
                .person1OtherNames(primary.getMiddleName())
                .person1MobilePhone(primary.getPhoneNumber())
                .person1EmailAddress(primary.getEmail())
                .person1AddressStreet(address1)
                .person1AddressCity(city1)
                .person1AddressCountryCd(countryCode1)
                .person1BvnNumber(encryptionUtil.decrypt(jointAccountDtos.getPrimary().getBvn()))
                .person1GenderCd(primaryProfile.getGender())

                .person2FirstName(secondary.getFirstName())
                .person2LastName(secondary.getLastName())
                .person2OtherNames(secondary.getMiddleName())
                .person2MobilePhone(secondary.getPhoneNumber())
                .person2EmailAddress(secondary.getEmail())
                .person2AddressStreet(address2)
                .person2AddressCity(city2)
                .person2AddressCountryCd(countryCode2)
                .person2GenderCd(secondaryProfile.getGender())

                .accountId(accountId)
                .build();

        try {
            OutboxEvent customer = OutboxEvent.builder()
                    .aggregateId(primary.getId()).aggregateType(AggregateType.USER.getValue())
                    .eventType(KafkaTopics.KAFKA_JOINT_CUSTOMER_CREATE_TOPIC)
                    .outboxStatus(OutboxStatus.PENDING.getValue())
                    .eventClass(CreateJointCustomerDto.class.getName())
                    .eventKey(primary.getId().toString())
                    .payload(objectMapper.writeValueAsString(createCustomerDto)).build();
            outboxEventRepository.save(customer);

            idDetailsService.buildAndSaveIdDetails(jointAccountDtos.getPrimary(), primary);
            idDetailsService.buildAndSaveIdDetails(jointAccountDtos.getSecondary(), secondary);

        } catch (JsonProcessingException e) {
            log.error("Error creating customer for user with id {} to outbox", primary.getId(), e);
            throw new RuntimeException("Please try again later");
        }
    }

    @Transactional
    @Override
    public void createCustomer(CreateCustomerDto value) {

        String customerId = usersRepository.findCustomerIdByEmail(value.email());
        if (nonNull(customerId)) {
            return;
        }
        CreateIndividualCustomerRequest request = CreateIndividualCustomerRequest.builder()
                .primaryEmailAddress(value.email()).firstName(value.firstName()).lastName(value.lastName()).otherNames(value.middleName())
                .mobilePhoneNo(value.phoneNumber()).genderCd(Gender.getGender(value.gender()).getAbbreviation())
                .addressStreet(value.addressStreet()).addressCity(value.addressCity())
                .addressCountryCd(value.addressCountryCd()).build();
        MiddlewareResponse<CreateIndividualCustomerResponse> response = middleWareClient.createIndividualCustomer(request);
        if ("success".equalsIgnoreCase(response.status())) {
            Users users = usersRepository.findUsersByEmail(value.email());
            users.setMiddlewareCustomerId(response.data().customerId());
            usersRepository.save(users);
        } else {
            throw new BadRequestException("Could not create customer");
        }
    }

    // TODO
    @Transactional
    @Override
    public void createJointCustomer(CreateJointCustomerDto value) {

//        String accountId = accountPartyRepository.findAccountPartyByAccountId(value.accountId());
//        if (nonNull(accountId)) {
//            return;
//        }
//
//        CreateJointCustomerRequest request = middlewareMapper.createJointCustomerDtoToCreateJointCustomerRequest(value);
//        MiddlewareResponse<CreateIndividualCustomerResponse> response = middleWareClient.createJointCustomer(request);
//        if ("success".equalsIgnoreCase(response.status())) {
//
//            usersRepository.updateAllCustomerId(response.data().customerId(), List.of(value.person1EmailAddress(), value.person2EmailAddress()));
//            accountPartyRepository.updateAllCustomerId(response.data().customerId(), value.accountId());
//
//            Cache cache = requireNonNull(cacheManager.getCache(AppConstants.USERS_CACHE_NAME), "could not be completed");
//            List<Long> userIds = usersRepository.findIdsByEmail(List.of(value.person1EmailAddress(), value.person2EmailAddress()));
//            userIds.forEach(cache::evict);
//        } else {
//            throw new BadRequestException("Could not create customer");
//        }
    }

    @Override
    public void addressVerified(CustomerAddressVerifiedDto value) {

        UpdateAddressRequest request = usersMapper.customerAddressVerifiedDtoToUpdateAddressRequest(value);
        MiddlewareResponse<MiddlewareBaseApiResponse> response = middleWareClient.updateIndividualCustomerAddress(request);
        if ("success".equalsIgnoreCase(response.status())) {
            log.info("Customer {} address updated", request.customerId());
        } else {
            throw new BadRequestException("Could not create customer");
        }
    }

    /**
     * Updates the current user's email if it has not yet been verified.
     * Evicts the users cache entry for the new email.
     *
     * @param userRequest payload with the new email
     * @return UpdateResponse indicating success
     * @throws BadRequestException if the existing email is already verified
     */
    public UpdateResponse updateEmail(UpdateEmailRequest userRequest) {
        log.info("User with email {} email update started", userRequest.oldEmail());

        Cache cache = requireNonNull(cacheManager.getCache(AppConstants.SIGN_UP_CACHE_NAME));

        IdQueryDetailsDto response = cache.get(userRequest.oldEmail(), IdQueryDetailsDto.class);

        if (response == null) {
            throw new AccessDeniedException("Initial sign up details not found.");
        }
        if (response.isEmailVerified()) {
            throw new BadRequestException("Email has already been verified.");
        }
        if (userRequest.newEmail().equals(userRequest.oldEmail())) {
            throw new BadRequestException("Email cannot be the same as your current email.");
        }

        if (usersRepository.existsByEmail(userRequest.newEmail())) {
            throw new BadRequestException("Email already in use.");
        }

        response.setEmail(userRequest.newEmail());
        cache.put(response.getEmail(), response);
        cache.evict(userRequest.oldEmail());
        log.info("User with email {} email update completed", userRequest.newEmail());
        return UpdateResponse.builder().success(true).message("Email successfully updated").build();
    }

    @Override
    public UpdateResponse updateJointPrimaryEmail(UpdateEmailRequest userRequest) {
        log.info("User with joint primary email {} email update started", userRequest.oldEmail());

        Cache cache = requireNonNull(cacheManager.getCache(AppConstants.JOINT_SIGN_UP_CACHE_NAME));
        CreateJointAccountDtos dtos = cache.get(userRequest.oldEmail(), CreateJointAccountDtos.class);
        if (dtos == null) {
            throw new AccessDeniedException("Initial sign up details not found.");
        }
        IdQueryDetailsDto response = dtos.getPrimary();

        if (isNull(response)) {
            throw new AccessDeniedException("Initial sign up details not found.");
        }
        if (response.isEmailVerified()) {
            throw new BadRequestException("Primary email has already been verified.");
        }
        if (userRequest.newEmail().equals(userRequest.oldEmail())) {
            throw new BadRequestException("Email cannot be the same as your current email.");
        }

        if (usersRepository.existsByEmail(userRequest.newEmail())) {
            throw new BadRequestException("Email already in use.");
        }

        response.setEmail(userRequest.newEmail());
        dtos.setPrimary(response);
        cache.put(response.getEmail(), dtos);
        cache.evict(userRequest.oldEmail());
        log.info("User with email {} email update completed for joint account", userRequest.newEmail());
        return UpdateResponse.builder().success(true).message("Email successfully updated").build();
    }


    /**
     * Retrieves the currently logged-in user's details.
     *
     * @return the user's response
     * @throws BadRequestException if the user is not found
     */
    public UsersResponse getUser() {
        UsersResponse response = usersRepository.findUserDetailsById(AppUtil.getLoggedInUserId()).orElseThrow(() -> new BadRequestException("User not found."));
        String signedUrl = null;
        if (StringUtils.hasText(response.image())) {
            SignedUrlResponse signedUrlResponse = huaweiService.getSignedUrl(SignedUrlRequest.builder().method(HttpMethodEnum.GET).fileName(response.image())
                    .type(SignedUrlType.IMAGE).build());
            signedUrl = signedUrlResponse.signedUrl();
        }
        boolean pinSet = userPinRepository.existsByUserId(response.id());
        boolean allDataShared = response.userInstrumentResponses().stream().allMatch(i -> i.dataSharingAllowed() == true);
        return UsersResponse.newResponse(response.status(), response.id(), response.email(), response.firstName(), response.lastName(), response.middleName(),
                response.phoneNumber(), signedUrl, response.gender(), response.dateOfBirth(), response.referralCode(),
                response.onboardingCompleted(), response.userInstrumentResponses(), allDataShared, response.userOptionResponses(), response.biometricEnabled(), pinSet,
                response.interestFreeInvestment(), response.interestFreeInvestmentSet(), response.cscsNumber(), response.chnNumber(), response.emailVerified(), response.accountType());
    }

    /**
     * Resets the user's password after validating the OTP and ensuring the new password is different.
     *
     * @param request the password reset request
     * @return the password reset response
     * @throws BadRequestException if OTP is invalid/expired, or the new password matches the old one
     */
    @Transactional
    public PasswordResetResponse resetPassword(PasswordResetRequest request) {

        log.info("Password reset started for user {}", request.recipient());
        // Ensure otp exists and not expired

        Cache cache = requireNonNull(cacheManager.getCache(AppConstants.OTP_CACHE_NAME), "Error getting otp");
        String cacheKey = request.recipient().concat(String.valueOf(MessageSubject.PASSWORD_RESET.getCode()));
        OtpVerificationDto otpVerificationDto = cache.get(cacheKey, OtpVerificationDto.class);

        if (isNull(otpVerificationDto)) {
            throw new BadRequestException("OTP not verified or expired.");
        }

        if (!otpVerificationDto.getVerified()) {
            throw new BadRequestException("OTP not verified or expired.");
        }

        // Ensure the password is different from the old one
        Users users = usersRepository.findUsersByEmailOrPhoneNumber(request.recipient(), request.recipient()).orElseThrow(() -> new BadRequestException("User not found."));
        if (passwordEncoder.matches(request.password(), users.getPassword())) {
            throw new BadRequestException("Password cannot be the same as your old password.");
        }

        // Update the password and expire otp
        usersRepository.updateUsersPassword(users.getEmail(), passwordEncoder.encode(request.password()));
        requireNonNull(cacheManager.getCache(AppConstants.USERS_CACHE_NAME)).evict(users.getId());
        // Notify the user about the password rest via mail
        notifyUserAboutPasswordChange(users.getEmail());
        log.info("Password reset completed for user {}", request.recipient());
        return PasswordResetResponse.builder().success(true).message("Password successfully updated.").build();
    }

    /**
     * Updates the logged-in user's password after ensuring it is different from the old one.
     *
     * @param request the update password request
     * @return the update password response
     * @throws BadRequestException if the new password matches the old one
     */
    @Transactional
    public UpdateResponse updatePassword(UpdatePasswordRequest request) {

        String userEmail = AppUtil.getLoggedInUserEmail();
        log.info("Password update started for user {}", userEmail);
        Long userId = AppUtil.getLoggedInUserId();
        String userPassword = usersRepository.findPasswordByEmailOrPhoneNumber(userEmail);

        // Ensure otp exists and not expired
        Cache cache = requireNonNull(cacheManager.getCache(AppConstants.OTP_CACHE_NAME), "Error getting otp");
        String cacheKey = userEmail.concat(String.valueOf(MessageSubject.PASSWORD_RESET.getCode()));
        OtpVerificationDto otpVerificationDto = cache.get(cacheKey, OtpVerificationDto.class);

        if (isNull(otpVerificationDto)) {
            throw new BadRequestException("OTP not verified or expired.");
        }

        if (!otpVerificationDto.getVerified()) {
            throw new BadRequestException("OTP not verified or expired.");
        }

        if (!passwordEncoder.matches(request.oldPassword(), userPassword)) {
            throw new BadRequestException("Wrong oldPassword entered.");
        }

        if (passwordEncoder.matches(request.newPassword(), userPassword)) {
            throw new BadRequestException("Password cannot be the same as your old oldPassword.");
        }

        // Update password and return
        usersRepository.updateUsersPassword(userEmail, passwordEncoder.encode(request.newPassword()));
        requireNonNull(cacheManager.getCache(AppConstants.USERS_CACHE_NAME)).evict(userId);

        // Notify the user about the password rest via mail
        notifyUserAboutPasswordChange(userEmail);
        log.info("Password update completed for user {}", userEmail);
        return UpdateResponse.builder().success(true).message("Password successfully updated.").build();
    }

    /**
     * Updates the logged-in user's phone number.
     *
     * @param request the update phone number request
     * @return the update phone number response
     */
    public UpdatePhoneNumberResponse updatePhoneNumber(UpdatePhoneNumberRequest request) {

        String userEmail = AppUtil.getLoggedInUserEmail();
        Long userId = AppUtil.getLoggedInUserId();

        usersRepository.updateUsersPhoneNumber(request.phoneNumber(), userEmail);
        requireNonNull(cacheManager.getCache(AppConstants.USERS_CACHE_NAME)).evict(userId);
        log.info("User with email {} updated phone number", userEmail);
        return UpdatePhoneNumberResponse.builder().status(true).message("User phone number updated").build();
    }

    /**
     * Updates the logged-in user's avatar URL after validating it.
     *
     * @param request the update avatar URL request
     * @return the update avatar URL response
     * @throws BadRequestException if the avatar URL is invalid
     */
    public UpdateImageResponse updateImage(UpdateImageRequest request) {

        Long userId = AppUtil.getLoggedInUserId();

        String imageKey;
        if (request.imageType() == FileType.AVATAR) {
            Files avatars = filesRepository.findByFileKeyAndFileType(request.imageKey(), FileType.AVATAR.getValue()).orElseThrow(() -> new BadRequestException("Avatar not found."));
            imageKey = avatars.getFileKey();
            userProfileRepository.updateUsersImage(imageKey, userId);
        } else {
            if (request.contentType() == null) {
                throw new BadRequestException("Content type not found.");
            }
            imageKey = request.imageKey();
            filesRepository.findByFileTypeAndUserId(FileType.PROFILE_PICTURE.getValue(), AppUtil.getLoggedInUserId())
                    .ifPresentOrElse(i -> {
                            },
                            () -> {
                                filesRepository.save(Files.builder().userId(userId).fileKey(imageKey).contentType(request.contentType()).fileType(FileType.PROFILE_PICTURE.getValue()).build());
                                userProfileRepository.updateUsersImage(imageKey, userId);
                            }
                    );
        }

        requireNonNull(cacheManager.getCache(AppConstants.USERS_CACHE_NAME)).evict(userId);
        return UpdateImageResponse.builder().status(true).message("User image updated").build();
    }

    /**
     * This is only for updating the user's newPin when they still know their old newPin.
     * Updates the logged-in user's PIN after ensuring it is different from the old one.
     *
     * @param request the update PIN request
     * @return the update PIN response
     * @throws BadRequestException if the new PIN matches the old one
     */
    public PinResponse updatePin(PinRequest request) {

        Long userId = AppUtil.getLoggedInUserId();

        Optional<UserPin> userPinOptional = userPinRepository.findByUserId(userId);
        UserPin userPin;

        if (userPinOptional.isPresent() && request.isNew().equals(AppConstants.IS_NEW_PIN)) {
            throw new BadRequestException("Pin has already been created for this account, you should update pin instead.");
        }

        if (userPinOptional.isEmpty() && request.isNew().equals(AppConstants.IS_UPDATE_PIN)) {
            throw new BadRequestException("You need to create a pin first.");
        }

        if (request.isNew().equals(AppConstants.IS_UPDATE_PIN)) {

            if (isNull(request.oldPin())) {
                throw new BadRequestException("You must pass the old pin to update your pin.");
            }
            // Ensure old matches
            if (!passwordEncoder.matches(request.oldPin(), userPinOptional.get().getPin())) {
                throw new BadRequestException("Wrong old pin entered.");
            }

            // Ensure the newPin is different from the old one
            if (passwordEncoder.matches(request.newPin(), userPinOptional.get().getPin())) {
                throw new BadRequestException("New pin cannot be the same as your old pin.");
            }
        }

        userPin = userPinOptional.orElse(UserPin.builder().userId(userId).build());
        userPin.setPin(passwordEncoder.encode(request.newPin()));
        userPinRepository.save(userPin);

        return PinResponse.builder().status(true).message("Pin successfully set.").build();
    }

    /**
     * Retrieves signed URLs for all available avatar images.
     * Results are cached under the "avatars" cache.
     *
     * @return list of signed URL responses for avatars
     */
    @Cacheable("avatars")
    public List<SignedUrlResponse> getAvatarUrls() {
        List<SignedUrlResponse> responses = new ArrayList<>();
        for (Files images : filesRepository.findAllByFileType(FileType.AVATAR.getValue())) {
            responses.add(huaweiService.getSignedUrl(SignedUrlRequest.builder().method(HttpMethodEnum.GET).fileName(images.getFileKey())
                    .type(SignedUrlType.IMAGE).build()));
        }
        return responses;
    }

    /**
     * Deactivates the currently logged-in user's account.
     *
     * @return response indicating whether the operation was successful
     */
    public AccountDeactivationResponse deactivateUser() {
        String userEmail = AppUtil.getLoggedInUserEmail();
        Long userId = AppUtil.getLoggedInUserId();
        int updated = usersRepository.updateUsersStatus(userEmail, UserStatus.DEACTIVATED.getValue());
        requireNonNull(cacheManager.getCache(AppConstants.USERS_CACHE_NAME)).evict(userId);
        return AccountDeactivationResponse.builder().message(updated == 1 ? "Successful" : "Failed").status(updated == 1).build();
    }

    /**
     * Completes onboarding for the specified user if all requirements are submitted,
     * updates the profile, evicts the cache entry, and emits a KYC_COMPLETED event.
     *
     * @param userId the user identifier
     */
    public void completeUserOnboarding(String userId) {

        KycCompletedDto kycCompletedDto = usersRepository.getUserKyc(userId);
        if (nonNull(kycCompletedDto) && userOnboardingRepository.allRequirementsSubmitted(kycCompletedDto.userId())) {
            userProfileRepository.completeOnboarding(kycCompletedDto.userId());
            usersRepository.updateUsersStatus(kycCompletedDto.userId(), UserStatus.ACTIVE.getValue());
            requireNonNull(cacheManager.getCache(AppConstants.USERS_CACHE_NAME)).evict(kycCompletedDto.userId());
            kafkaSenderService.send(kycCompletedDto, Map.of(KafkaHeaders.TOPIC, KafkaTopics.KAFKA_KYC_COMPLETED, KafkaHeaders.KEY, String.valueOf(userId)));
        }
    }

    /**
     * Resets onboarding for the specified user and marks a specific requirement as REJECTED.
     * Updates the profile status, evicts the cache entry, and emits a KYC_REJECTED event.
     *
     * @param userId        the user identifier
     * @param requirementId the requirement that was rejected
     */
    public void resetUserOnboarding(String userId, Long requirementId) {

        KycCompletedDto kycCompletedDto = usersRepository.getUserKyc(userId);
        if (nonNull(kycCompletedDto)) {
            userProfileRepository.resetOnboarding(kycCompletedDto.userId());
            usersRepository.updateUsersStatus(kycCompletedDto.userId(), UserStatus.KYC_NOT_COMPLETED.getValue());
            userOnboardingRepository.updateUserOnboardingStatus(kycCompletedDto.userId(), requirementId, OnboardingStatus.REJECTED.getValue(), UserOnboardingNotes.FAILED.note, false);
            requireNonNull(cacheManager.getCache(AppConstants.USERS_CACHE_NAME)).evict(kycCompletedDto.userId());
            kafkaSenderService.send(kycCompletedDto, Map.of(KafkaHeaders.TOPIC, KafkaTopics.KAFKA_KYC_REJECTED, KafkaHeaders.KEY, String.valueOf(userId)));
        }
    }

    /**
     * Updates the logged-in user's state of origin after validating it against the selected country.
     *
     * @param request payload containing the state and country identifiers
     * @return UpdateResponse indicating whether the update succeeded
     */
    public UpdateResponse updateStateOfOrigin(StateUpdateRequest request) {
        AtomicInteger updated = new AtomicInteger();
        generalRepository.findOneBy(CountryStates.class, Map.of("id", request.stateId(), "countryId", request.countryId()))
                .ifPresent(countryStates -> updated.set(userProfileRepository.updateState(AppUtil.getLoggedInUserId(), countryStates.getName())));
        clearUsersCache();
        return UpdateResponse.builder().success(updated.get() != 0).message(updated.get() != 0 ? "Successful" : "Failed").build();
    }

    /**
     * Updates the logged-in user's country of origin.
     *
     * @param request payload containing the country identifier
     * @return UpdateResponse indicating whether the update succeeded
     */
    public UpdateResponse updateCountryOfOrigin(CountryUpdateRequest request) {

        AtomicInteger updated = new AtomicInteger();
        generalRepository.findById(Countries.class, request.id()).ifPresent(country -> updated.set(userProfileRepository.updateCountry(AppUtil.getLoggedInUserId(), country.getName())));
        clearUsersCache();
        return UpdateResponse.builder().success(updated.get() != 0).message(updated.get() != 0 ? "Successful" : "Failed").build();
    }

    /**
     * Sends a password change notification email event for the specified user.
     *
     * @param userEmail recipient email address
     */
    private void notifyUserAboutPasswordChange(String userEmail) {
        Long userId = usersRepository.findIdByEmail(userEmail);
        PasswordChangeDto otpDto = PasswordChangeDto.builder().recipient(new String[]{userEmail})
                .body("Your password was changed, if you didn't initiate this, click this link.")
                .subject(MessageSubjects.PASSWORD_RESET).build();
        MessageDto messageDto = MessageDto.builder().medium(MessageMedium.EMAIL).isHtml(true).type(MessageType.PASSWORD_RESET).message(otpDto).classSimpleName(PasswordChangeDto.class.getSimpleName()).build();
        kafkaSenderService.send(messageDto, Map.of(KafkaHeaders.TOPIC, KafkaTopics.KAFKA_SUCCESSFUL_PASSWORD_RESET, KafkaHeaders.KEY, String.valueOf(userId)));
    }

    /**
     * Marks an investment instrument as accessed for the current user and evicts the users cache entry.
     *
     * @param request payload containing the instrument access identifier
     * @return UpdateResponse indicating whether the update succeeded
     */
    public UpdateResponse updateUserInstrument(UserInstrumentRequest request) {

        Map<String, Object> updates = new HashMap<>();
        updates.put("accessed", true);
        clearUsersCache();
        return getUpdateResponse(updates, request.instrumentId());
    }

    /**
     * Marks an investment option as accessed for the current user and evicts the users cache entry.
     *
     * @param request payload containing the option access identifier
     * @return UpdateResponse indicating whether the update succeeded
     */
    public UpdateResponse updateOptionAccessed(OptionAccessedRequest request) {

        Map<String, Object> updates = new HashMap<>();
        updates.put("accessed", true);
        int updated = customRepository.dynamicUpdate(InvestmentOptionsAccessed.class, updates, Map.of("option_id", request.optionId(), "user_id", AppUtil.getLoggedInUserId()));
        clearUsersCache();
        return UpdateResponse.builder().success(updated != 0).message(updated != 0 ? "Successful" : "Failed").build();
    }

    /**
     * Enables or disables biometric login for the current user and evicts the users cache entry.
     *
     * @param request payload indicating whether biometric login should be enabled
     * @return UpdateResponse indicating whether the update succeeded
     */
//    public UpdateResponse updateBiometricOfOrigin(BiometricLoginUpdateRequest request) {
//
//        Map<String, Object> updates = new HashMap<>();
//        updates.put("biometric_enabled", request.biometricLogin());
//        return getUpdateResponse(updates);
//    }
    public UpdateResponse updateDataSharing(DataSharingRequest request) {

        Map<String, Object> updates = new HashMap<>();
        updates.put("data_sharing_allowed", request.dataSharing());
        clearUsersCache();
        return getUpdateResponse(updates, request.instrumentId());
    }

    private UpdateResponse getUpdateResponse(Map<String, Object> updates, Long aLong) {
        int updated = customRepository.dynamicUpdate(UserInstrument.class, updates, Map.of("instrument_id", aLong, "user_id", AppUtil.getLoggedInUserId()));
        clearUsersCache();
        return UpdateResponse.builder().success(updated != 0).message(updated != 0 ? "Successful" : "Failed").build();
    }

    private void clearUsersCache() {
        requireNonNull(cacheManager.getCache(AppConstants.USERS_CACHE_NAME)).evict(AppUtil.getLoggedInUserId());
    }

    public UpdateResponse updateDataSharing(ShareAllDataRequest request) {

        Long userId = usersRepository.findIdByEmail(request.userEmail());
        if (isNull(userId)) {
            throw new BadRequestException("User does not exist.");
        }
        Map<String, Object> updates = new HashMap<>();
        updates.put("data_sharing_allowed", true);
        int updated = customRepository.dynamicUpdate(UserInstrument.class, updates, Map.of("user_id", userId));
        usersRepository.updateUsersStatus(userId, UserStatus.KYC_NOT_COMPLETED.getValue());
        clearUsersCache();
        return UpdateResponse.builder().success(updated != 0).message(updated != 0 ? "Successful" : "Failed").build();
    }

    public UpdateResponse interestFree(InterestSharingRequest request) {

        Map<String, Object> updates = new HashMap<>();
        updates.put("interest_free_investment", request.wantInterest());
        clearUsersCache();
        return getUpdateResponse(updates);
    }

    private UpdateResponse getUpdateResponse(Map<String, Object> updates) {
        int updated = customRepository.dynamicUpdate(UserProfile.class, updates, Map.of("user_id", AppUtil.getLoggedInUserId()));
        clearUsersCache();
        return UpdateResponse.builder().success(updated != 0).message(updated != 0 ? "Successful" : "Failed").build();
    }

    @Transactional
    public UpdateResponse verifyPin(VerifyPinRequest request) {
        Long userId;

        try {
            userId = AppUtil.getLoggedInUserId();
            if (isNull(userId)) {
                userId = request.userId();
            }
        } catch (BadRequestException e) {
            userId = request.userId();
        }

        Optional<UserPin> userPinOpt = userPinRepository.findByUserId(userId);
        if (userPinOpt.isEmpty()) {
            return UpdateResponse.builder().success(false).message("Invalid pin").build();
        }
        UserPin userPin = userPinOpt.get();

        if (Objects.equals(userPin.getStatus(), UserPinStatus.LOCKED.getStatus())) {
            LocalDateTime lockUntil = userPin.getLockUntil();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            return UpdateResponse.builder().success(false).message("Pin is locked, you can retry after %s".formatted(formatter.format(lockUntil))).build();
        }

        boolean matches = passwordEncoder.matches(request.pin(), userPin.getPin());
        if (!matches) {
            int failedAttempts = userPin.getFailedAttempts();
            userPin.setFailedAttempts(++failedAttempts);
            userPin.setLastFailedAt(LocalDateTime.now());

            if (Objects.equals(userPin.getFailedAttempts(), AppConstants.MAX_PIN_FAILED_ATTEMPTS_B4_LOCK)) {
                userPin.setLockUntil(LocalDateTime.now().plusMinutes(AppConstants.PIN_LOCKED_MAX_TIME_IN_MINS));
                userPin.setStatus(UserPinStatus.LOCKED.getStatus());
                userPinRepository.save(userPin);
                LocalDateTime lockUntil = userPin.getLockUntil();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                return UpdateResponse.builder().success(false).message("Pin is locked, you can retry after %s".formatted(formatter.format(lockUntil))).build();
            }

            userPinRepository.save(userPin);
            return UpdateResponse.builder().success(false).message("Invalid pin, %d attempts remaining"
                    .formatted((AppConstants.MAX_PIN_FAILED_ATTEMPTS_B4_LOCK - userPin.getFailedAttempts()))).build();
        } else {
            userPin.setFailedAttempts(0);
            userPinRepository.save(userPin);
            return UpdateResponse.builder().success(true).message("Pin verified").build();
        }

    }

    public StageResponse processDetails(String email) {

        Cache cache = requireNonNull(cacheManager.getCache(AppConstants.SIGN_UP_CACHE_NAME));
        IdQueryDetailsDto ninQueryResponse = cache.get(email, IdQueryDetailsDto.class);

        if (ninQueryResponse == null) {
            throw new AccessDeniedException("Initial sign up details not found.");
        }

        if (!ninQueryResponse.isEmailVerified()) {
            return new StageResponse(OnboardingStage.EMAIL);
        }
        return new StageResponse(OnboardingStage.PASSWORD);
    }

    public UpdateResponse verifyPassword(VerifyPasswordRequest request) {

        boolean matches = passwordEncoder.matches(request.password(), usersRepository
                .findPasswordById(AppUtil.getLoggedInUserId()));
        return UpdateResponse.builder().success(matches).message(matches ? "Password verified" : "Invalid Password").build();
    }

    @Override
    public UpdateResponse updateCscs(UpdateCscsRequest request) {
        Map<String, Object> updates = new HashMap<>();
        updates.put("chn_number", request.chnNumber());
        clearUsersCache();
        return getUpdateResponse(updates);
    }

    @Override
    public UpdateResponse createSpouse(CreateSpouseRequest request) {

        Long userId = AppUtil.getLoggedInUserId();

        Map<String, Object> updates = new HashMap<>();
        updates.put("marital_status", request.maritalStatus().getNumber());
        if (request.maritalStatus() == MaritalStatus.MARRIED) {
            List<String> errors = new ArrayList<>();
            if (isNull(request.title())) {
                errors.add("title: Kindly pass the title");
            }
            if (isBlank(request.fullName())) {
                errors.add("fullName: Kindly pass the fullName");
            }
            if (isBlank(request.email())) {
                errors.add("email: Kindly pass the email");
            }
            if (nonNull(request.nationalityId())) {
                errors.add("nationality: Kindly pass the nationality");
            }
            boolean countryDoesNotExist = !customRepository.existById(Countries.class, request.nationalityId());
            if (countryDoesNotExist) {
                errors.add("nationality: Nationality does not exist");
            }
            if (isBlank(request.phoneNumber())) {
                errors.add("phoneNumber: Kindly pass the phoneNumber");
            }
            if (isBlank(request.phoneNumberFormat())) {
                errors.add("phoneNumberFormat: Kindly pass the phoneNumberFormat");
            }
            if (!request.validateData() || countryDoesNotExist) {
                throw new ContextException("Kindly pass the required values", errors);
            }
            Spouse spouse = Spouse.builder()
                    .title(request.title().getNumber()).email(request.email()).phoneNumber(request.phoneNumber())
                    .userId(userId).fullName(request.fullName()).nationalityId(request.nationalityId())
                    .build();
            customRepository.save(spouse);
        }
        clearUsersCache();
        return getUpdateResponse(updates);
    }

    private void validateAndMarkEmailAsVerifiedJoint(String userId) {
        Cache cache = Objects.requireNonNull(cacheManager.getCache(AppConstants.JOINT_SIGN_UP_CACHE_NAME));
        CreateJointAccountDtos jointAccountDtos = cache.get(userId, CreateJointAccountDtos.class);
        if (jointAccountDtos == null) {
            throw new AccessDeniedException("Initial sign up details not found.");
        }
        jointAccountDtos.getPrimary().setEmailVerified(true);
        cache.put(userId, jointAccountDtos);
        kafkaSenderService.send(new OtpVerifiedDto(userId), Map.of(KafkaHeaders.TOPIC, KafkaTopics.KAFKA_OTP_VERIFIED_TOPIC, KafkaHeaders.KEY, userId));
    }

    private void validateAndMarkEmailAsVerified(String userId) {
        Cache cache = Objects.requireNonNull(cacheManager.getCache(AppConstants.SIGN_UP_CACHE_NAME));
        IdQueryDetailsDto ninQueryResponse = cache.get(userId, IdQueryDetailsDto.class);
        if (ninQueryResponse == null) {
            throw new AccessDeniedException("Initial sign up details not found.");
        }
        ninQueryResponse.setEmailVerified(true);
        cache.put(userId, ninQueryResponse);
        kafkaSenderService.send(new OtpVerifiedDto(userId), Map.of(KafkaHeaders.TOPIC, KafkaTopics.KAFKA_OTP_VERIFIED_TOPIC, KafkaHeaders.KEY, userId));
    }

    private void validateAndMarkEmailAsVerifiedExisting(String recipient) {

        Cache cache = requireNonNull(cacheManager.getCache(AppConstants.EXISTING_USER_SIGN_UP_CACHE_NAME));
        MiddlewareCustomerResponse.CustomerData data = cache.get(recipient, MiddlewareCustomerResponse.CustomerData.class);
        if (data == null) {
            throw new AccessDeniedException("Process failed.");
        }
        data.setEmailVerified(true);
        cache.put(recipient, data);
        kafkaSenderService.send(new OtpVerifiedDto(recipient), Map.of(KafkaHeaders.TOPIC, KafkaTopics.KAFKA_OTP_VERIFIED_TOPIC, KafkaHeaders.KEY, recipient));
    }

    private void checkEmailOrPhoneDoesNotExist(String email, String phoneNumber) {
        log.info("First stage of User with email creation started {}", email);
        if (usersRepository.existsByEmailOrPhoneNumber(email, phoneNumber)) {
            throw new BadRequestException("Email or Phone number already exists " + email + " - " + phoneNumber);
        }
    }
}