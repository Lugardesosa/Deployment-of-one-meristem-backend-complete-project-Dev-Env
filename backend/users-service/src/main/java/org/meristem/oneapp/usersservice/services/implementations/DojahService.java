package org.meristem.oneapp.usersservice.services.implementations;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.meristem.oneapp.usersservice.constants.AppConstants;
import org.meristem.oneapp.usersservice.domains.enums.*;
import org.meristem.oneapp.usersservice.domains.enums.Vendor;
import org.meristem.oneapp.usersservice.domains.requests.IdQueryRequest;
import org.meristem.oneapp.usersservice.domains.requests.NinValidationRequest;
import org.meristem.oneapp.usersservice.domains.requests.TaxIdQueryRequest;
import org.meristem.oneapp.usersservice.domains.responses.BvnQueryResponse;
import org.meristem.oneapp.usersservice.domains.responses.IdValidationResponse;
import org.meristem.oneapp.usersservice.domains.responses.TaxIdQueryResponse;
import org.meristem.oneapp.usersservice.dtos.IdQueryDetailsDto;
import org.meristem.oneapp.usersservice.exception.exceptions.BadRequestException;
import org.meristem.oneapp.usersservice.integrations.DojahClient;
import org.meristem.oneapp.usersservice.integrations.requests.DojahBvnVerificationRequest;
import org.meristem.oneapp.usersservice.integrations.responses.DojahBvnLookUpResponse;
import org.meristem.oneapp.usersservice.integrations.responses.DojahBvnVerificationResponse;
import org.meristem.oneapp.usersservice.integrations.responses.DojahNinLookUpResponse;
import org.meristem.oneapp.usersservice.mappers.UserIdDetailsMapper;
import org.meristem.oneapp.usersservice.models.*;
import org.meristem.oneapp.usersservice.repositories.*;
import org.meristem.oneapp.usersservice.services.IIdDetailsService;
import org.meristem.oneapp.usersservice.services.IKycService;
import org.meristem.oneapp.usersservice.services.IUsersService;
import org.meristem.oneapp.usersservice.utils.AppUtil;
import org.meristem.oneapp.usersservice.utils.EncryptionUtil;
import org.meristem.oneapp.usersservice.utils.HashingUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static java.util.Objects.isNull;


/**
 * Service class for handling Smile ID-related operations, including generating Smile ID links,
 * processing webhook notifications, and managing user onboarding requirements.
 *
 * <p>This class integrates with Smile ID's API to create smart links for user verification,
 * handle webhook notifications for verification results, and update user records accordingly.</p>
 *
 * <p>Annotated with {@link Service} to indicate that it is a Spring-managed service component.
 * Transactional methods ensure atomicity for database operations.</p>
 *
 * <p>Dependencies are injected via constructor injection, and the class uses {@link Slf4j}
 * for logging purposes.</p>
 */
@Slf4j
@Service("DOJAH")
@Transactional
public class DojahService implements IKycService {

    private final CustomRepository customRepository;
    private final UserIdDetailsMapper userIdDetailsMapper = UserIdDetailsMapper.INSTANCE;
    private final UserProfileRepository userProfileRepository;
    private final KycQueryRepository kycQueryRepository;
    private final AmlVendorRepository amlVendorRepository;
    @Value("${hashing.id-hash-key}")
    private String idHashKey;

    public DojahService(CustomRepository customRepository, UserProfileRepository userProfileRepository, KycQueryRepository kycQueryRepository, AmlVendorRepository amlVendorRepository, UserOnboardingRepository userOnboardingRepository, RequirementsRepository requirementsRepository, UsersRepository usersRepository, CacheManager cacheManager, IdCardRepository idCardRepository, @Lazy IUsersService usersService, HashingUtil hashingUtil, EncryptionUtil encryptionUtil, IIdDetailsService idDetailsService, HttpServletRequest httpRequest, DojahClient dojahClient) {
        this.customRepository = customRepository;
        this.userProfileRepository = userProfileRepository;
        this.kycQueryRepository = kycQueryRepository;
        this.amlVendorRepository = amlVendorRepository;
        this.userOnboardingRepository = userOnboardingRepository;
        this.requirementsRepository = requirementsRepository;
        this.usersRepository = usersRepository;
        this.cacheManager = cacheManager;
        this.idCardRepository = idCardRepository;
        this.usersService = usersService;
        this.hashingUtil = hashingUtil;
        this.encryptionUtil = encryptionUtil;
        this.idDetailsService = idDetailsService;
        this.httpRequest = httpRequest;
        this.dojahClient = dojahClient;
    }

    private final UserOnboardingRepository userOnboardingRepository;
    private final RequirementsRepository requirementsRepository;
    private final UsersRepository usersRepository;
    private final CacheManager cacheManager;
    private final IdCardRepository idCardRepository;
    private final IUsersService usersService;
    private final HashingUtil hashingUtil;
    private final EncryptionUtil encryptionUtil;
    private final IIdDetailsService idDetailsService;
    private final HttpServletRequest httpRequest;


    private final DojahClient dojahClient;

    @Override
    public BvnQueryResponse bvnQuery(IdQueryRequest request) {

        if (IdCardType.BVN.compareTo(IdCardType.fromName(request.idType())) != 0) {
            throw new BadRequestException("Only BVN can be validated.");
        }


        Optional<IdCard> idCard = idCardRepository.findByIdValueHashedAndIdCardType(hashingUtil.hmacWithSha256(idHashKey, request.idNumber()), IdCardType.BVN.getName());
        if (idCard.isPresent()) {
            if (request.isPrimary()) {
                throw new BadRequestException("BVN already exists.");
            } else {
                IdQueryDetailsDto dto = usersRepository.findIdUserDetailById(idCard.get().getUserId());

                dto.setIdType(IdCardType.BVN.getName());
                return getBvnQueryResponse(cacheManager, request, dto, encryptionUtil, hashingUtil, idHashKey);
            }
        }
        DojahBvnLookUpResponse response = dojahClient.dojahBvnLookUp(request.idNumber());

        IdQueryDetailsDto dto = userIdDetailsMapper.dojahBvnLookupResponseToIdQueryDetailsDto(response.entity());
        dto.setIdType(IdCardType.BVN.getName());

        return getBvnQueryResponse(cacheManager, request, dto, encryptionUtil, hashingUtil, idHashKey);
    }

    @Override
    @Transactional
    public IdValidationResponse bvnValidation(MultipartFile file) {

        Cache cache = cacheManager.getCache(AppConstants.ID_VERIFICATION_CACHE_NAME);

        String loggedInUserEmail = AppUtil.getLoggedInUserEmail();
        assert cache != null;
        LocalDateTime expireIn = cache.get(loggedInUserEmail.concat(OnboardingRequirements.BVN.getName()), LocalDateTime.class);

        if (isNull(expireIn)) {
            expireIn = LocalDateTime.now().plusMinutes(AppConstants.ID_VERIFICATION_CACHE_EXPIRES_IN);
            cache.put(loggedInUserEmail.concat(OnboardingRequirements.BVN.getName()), expireIn);
        } else if (expireIn.isAfter(LocalDateTime.now())) {
            throw new BadRequestException("Try again at " + DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(expireIn));
        }

        Long loggedInUserId = AppUtil.getLoggedInUserId();

        org.meristem.oneapp.usersservice.models.Vendor vendor = amlVendorRepository.findAmlVendorByVendorCode(Vendor.SMILE_ID.getValue());
        Long requirementId = requirementsRepository.findIdByRequirementName(OnboardingRequirements.BVN.getName());
        String jobId = UUID.randomUUID().toString();
        KycQuery kycQuery = kycQueryRepository.save(KycQuery.builder().jobId(jobId).investmentRequirementId(requirementId).userId(loggedInUserEmail)
                .status(KycQueryStatus.PENDING.getValue()).vendorId(vendor.getId()).build());
        try {

            byte[] imageBytes = file.getBytes();
            String imageBase64 = Base64.getEncoder().encodeToString(imageBytes);

            String idNumber = idCardRepository.findIdCardValueByUserId(loggedInUserId, IdCardType.BVN.getName());
            String bvn = encryptionUtil.decrypt(idNumber);

            DojahBvnVerificationResponse response = dojahClient.dojahBvnVerify(DojahBvnVerificationRequest.builder().bvn(bvn).selfieImage(imageBase64).build());
            if (response.entity().selfieVerification().match()) {

                kycQuery.setMessage("Successful");
                kycQuery.setStatus(KycQueryStatus.COMPLETED.getValue());
                kycQueryRepository.save(kycQuery);
                userOnboardingRepository.updateUserOnboardingStatus(loggedInUserId, kycQuery.getInvestmentRequirementId(), OnboardingStatus.APPROVED.getValue(), UserOnboardingNotes.APPROVED.note, true);
                usersService.completeUserOnboarding(loggedInUserEmail, AppUtil.getInvestmentId(httpRequest), OnboardingRequirements.BVN);
                cache.evict(loggedInUserEmail.concat(OnboardingRequirements.BVN.getName()));
                return IdValidationResponse.builder().message("Successful").success(true).build();
            } else {
                kycQuery.setMessage("Failed");
                kycQuery.setStatus(KycQueryStatus.FAILED.getValue());
                kycQueryRepository.save(kycQuery);
                return IdValidationResponse.builder().message("Failed").success(false).build();
            }
        } catch (IOException e) {
            throw new BadRequestException("Could not complete bvn verification");
        }
    }

    @Override
    public TaxIdQueryResponse taxIdQuery(TaxIdQueryRequest request) {
        return null;
    }

    /**
     * Validates NIN data; flags mismatches; completes onboarding if valid
     */
    @Override
    public IdValidationResponse validateNin(NinValidationRequest request) {
        Cache cache = getIdQueryCache(cacheManager);

        if (idCardRepository.existsByIdValueHashedAndIdCardTypeAndUserIdNot(hashingUtil.hmacWithSha256(idHashKey, request.idNumber()), IdCardType.NIN.getName(), AppUtil.getLoggedInUserId())) {
            throw new BadRequestException("NIN already exists.");
        }

        DojahNinLookUpResponse response = dojahClient.dojahNinLookUpAdvance(request.idNumber());

        IdQueryDetailsDto dto = userIdDetailsMapper.dojahNinLookupResponseToIdQueryDetailsDto(response.entity());
        dto.setIdType(IdCardType.NIN.getName());
        Users loggedInUser = usersRepository.findById(AppUtil.getLoggedInUserId()).orElseThrow(() -> new AuthorizationDeniedException("User is not logged in"));
        UserProfile userProfile = userProfileRepository.findByUserId(loggedInUser.getId()).orElseThrow(() -> new BadRequestException("Invalid user."));

        userProfile.setTaxId(response.entity().taxId());
        userProfileRepository.save(userProfile);
        UserIdDetails bvn = customRepository.findOneBy(UserIdDetails.class, Map.of("userId", loggedInUser.getId(), "idType", IdCardType.BVN.getName())).orElseThrow(() -> new BadRequestException("BVN details could not be found."));

        List<String> names = AppUtil.buildNames(bvn.getFirstName(), bvn.getMiddleName(), bvn.getLastName());

        UserIdDetails nin = idDetailsService.buildAndSaveIdDetails(dto, loggedInUser);
        return compareNinAndBvnDetailsSaveAndReturn(cache, nin, names, bvn, loggedInUser, requirementsRepository, userOnboardingRepository, usersService, customRepository, idCardRepository, encryptionUtil.encrypt(request.idNumber()), hashingUtil.hmacWithSha256(idHashKey, request.idNumber()), AppUtil.getInvestmentId(httpRequest));
    }

    @Override
    public IdQueryDetailsDto ninQuery(String nin) {


        IdQueryDetailsDto dto = userIdDetailsMapper.dojahNinLookupResponseToIdQueryDetailsDto(dojahClient.dojahNinLookUp(nin).entity());
        dto.setIdType(IdCardType.NIN.getName());
        return dto;
    }
}
