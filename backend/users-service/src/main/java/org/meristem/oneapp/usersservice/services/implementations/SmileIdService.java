package org.meristem.oneapp.usersservice.services.implementations;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.meristem.oneapp.kafka.dtos.WebSocketDto;
import org.meristem.oneapp.usersservice.config.MaskingUtils;
import org.meristem.oneapp.usersservice.config.configProperties.SmileIdProperties;
import org.meristem.oneapp.usersservice.constants.AppConstants;
import org.meristem.oneapp.usersservice.constants.KafkaTopics;
import org.meristem.oneapp.usersservice.domains.enums.*;
import org.meristem.oneapp.usersservice.domains.enums.Vendor;
import org.meristem.oneapp.usersservice.domains.requests.*;
import org.meristem.oneapp.usersservice.domains.responses.*;
import org.meristem.oneapp.usersservice.dtos.IdQueryDetailsDto;
import org.meristem.oneapp.usersservice.exception.exceptions.BadRequestException;
import org.meristem.oneapp.usersservice.exception.exceptions.ResourceNotFoundException;
import org.meristem.oneapp.usersservice.exception.exceptions.UpstreamServiceException;
import org.meristem.oneapp.usersservice.integrations.SmileIdClient;
import org.meristem.oneapp.usersservice.integrations.requests.SmileIdEnhancedKycRequest;
import org.meristem.oneapp.usersservice.mappers.UserIdDetailsMapper;
import org.meristem.oneapp.usersservice.models.*;
import org.meristem.oneapp.usersservice.repositories.*;
import org.meristem.oneapp.usersservice.services.IIdDetailsService;
import org.meristem.oneapp.usersservice.services.IKafkaSenderService;
import org.meristem.oneapp.usersservice.services.IKycService;
import org.meristem.oneapp.usersservice.services.IUsersService;
import org.meristem.oneapp.usersservice.utils.AppUtil;
import org.meristem.oneapp.usersservice.utils.EncryptionUtil;
import org.meristem.oneapp.usersservice.utils.HashingUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Lazy;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import smile.identity.core.WebApi;
import smile.identity.core.enums.ImageType;
import smile.identity.core.enums.JobType;
import smile.identity.core.models.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.Optional;

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;


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
@Service("SMILE_ID")
@Transactional
public class SmileIdService implements IKycService {

    public static final String ID_APPROVED_STATUS = "1012";
    private final CustomRepository customRepository;
    private final SmileIdProperties smileIdProperties;
    private final AmlVendorRepository amlVendorRepository;
    private final UserIdDetailsMapper userIdDetailsMapper = UserIdDetailsMapper.INSTANCE;
    private final SimpMessagingTemplate messagingTemplate;
    @Value("${one-app.users-service.smile-id.server-ips}")
    private List<String> smileIps;

    @Value("${hashing.id-hash-key}")
    private String idHashKey;

    @Value("${spring.cloud.config.profile}")
    private String profile;

    private static final String DOCUMENT_APPROVED_STATUS = "0810";
    private static final Integer ENHANCED_JOB_TYPE = 5;

    private final KycQueryRepository kycQueryRepository;
    private final UserOnboardingRepository userOnboardingRepository;
    private final RequirementsRepository requirementsRepository;
    private final IdCardRepository idCardRepository;
    private final UserProfileRepository userProfileRepository;
    private final UsersRepository usersRepository;
    private final CacheManager cacheManager;
    private final IUsersService usersService;
    private final HttpServletRequest httpServletRequest;
    private final IKafkaSenderService kafkaSenderService;
    private final SmileIdClient smileIdClient;
    private final HashingUtil hashingUtil;
    private final EncryptionUtil encryptionUtil;
    private final IIdDetailsService idDetailsService;

    public SmileIdService(
            CustomRepository customRepository,
            SmileIdProperties smileIdProperties,
            AmlVendorRepository amlVendorRepository,
            KycQueryRepository kycQueryRepository,
            UserOnboardingRepository userOnboardingRepository,
            RequirementsRepository requirementsRepository,
            IdCardRepository idCardRepository,
            UserProfileRepository userProfileRepository,
            UsersRepository usersRepository,
            CacheManager cacheManager,
            @Lazy IUsersService usersService,
            HttpServletRequest httpServletRequest,
            IKafkaSenderService kafkaSenderService,
            SmileIdClient smileIdClient,
            HashingUtil hashingUtil,
            EncryptionUtil encryptionUtil,
            IIdDetailsService idDetailsService, SimpMessagingTemplate messagingTemplate) {
        this.customRepository = customRepository;
        this.smileIdProperties = smileIdProperties;
        this.amlVendorRepository = amlVendorRepository;
        this.kycQueryRepository = kycQueryRepository;
        this.userOnboardingRepository = userOnboardingRepository;
        this.requirementsRepository = requirementsRepository;
        this.idCardRepository = idCardRepository;
        this.userProfileRepository = userProfileRepository;
        this.usersRepository = usersRepository;
        this.cacheManager = cacheManager;
        this.usersService = usersService;
        this.httpServletRequest = httpServletRequest;
        this.kafkaSenderService = kafkaSenderService;
        this.smileIdClient = smileIdClient;
        this.hashingUtil = hashingUtil;
        this.encryptionUtil = encryptionUtil;
        this.idDetailsService = idDetailsService;
        this.messagingTemplate = messagingTemplate;
    }

    List<String> dataStatus = List.of(ID_APPROVED_STATUS, DOCUMENT_APPROVED_STATUS);
    List<String> actionStatus = List.of("1210", DOCUMENT_APPROVED_STATUS);
    List<String> errorCodes = List.of("1013", "1014");

    List<String> rejectionsStatus = List.of("1211", "1212", "1213", "0911", "0912", "0811", "0813", "0811", "0812", "1014");

    public BvnQueryResponse bvnQuery(IdQueryRequest request) {
        if (IdCardType.BVN.compareTo(IdCardType.fromName(request.idType())) != 0) {
            throw new BadRequestException("Only BVN can be validated. ");
        }

        BvnQueryResponse resp = getBvnResponse(cacheManager, encryptionUtil, request, idCardRepository, hashingUtil, idHashKey, usersRepository);
        if (nonNull(resp)) {
            return resp;
        }

        SmileIdWebhookNotification response = getSmileIdWebhookNotification(request, IdCardType.BVN, null);
        if (ID_APPROVED_STATUS.equals(response.getResultCode()) && confirmSignature(response.getSignature(), response.getTimestamp())) {

            IdQueryDetailsDto dto = userIdDetailsMapper.smileIdBvnLookupResponseToIdQueryDetailsDto(response);
            dto.setIdType(IdCardType.BVN.getName());
            return getBvnQueryResponse(cacheManager, request, dto, encryptionUtil, hashingUtil, idHashKey);
        } else if (errorCodes.contains(response.getResultCode())) {
            throw new ResourceNotFoundException("Enter a valid bvn", request.idType(), request.idNumber());
        } else {
            throw new BadRequestException("Try again later.");
        }
    }

    @Override
    public IdValidationResponse bvnValidation(MultipartFile file) {
        return null;
    }

    @Override
    public TaxIdQueryResponse taxIdQuery(TaxIdQueryRequest request) {
        return null;
    }

    private SmileIdWebhookNotification getSmileIdWebhookNotification(IdQueryRequest request, IdCardType idCardType, Long userId) {

        Optional<IdCard> idCard = idCardRepository.findByIdValueHashedAndIdCardType(hashingUtil.hmacWithSha256(idHashKey, request.idNumber()), IdCardType.NIN.getName());

        if (idCard.isPresent()) {
            if (Objects.equals(idCard.get().getUserId(), AppUtil.getLoggedInUserId())) {
                IdQueryDetailsDto dto = usersRepository.findIdUserDetailById(idCard.get().getUserId());
                SmileIdWebhookNotification response = userIdDetailsMapper.idQueryDetailsDtoToSmileIdWebhookNotification(dto);
                response.setResultCode(ID_APPROVED_STATUS);
                return response;
            } else {
                throw new BadRequestException(idCardType.getName() + " already exists");
            }
        }

        if (idCardRepository.existsByIdValueHashedAndIdCardType(hashingUtil.hmacWithSha256(idHashKey, request.idNumber()), idCardType.getName())) {
            throw new BadRequestException(idCardType.getName() + " already exists.");
        }
        SmileIdEnhancedKycRequest.PartnerParams partnerParams = SmileIdEnhancedKycRequest.PartnerParams.builder()
                .job_id(UUID.randomUUID().toString())
                .job_type(ENHANCED_JOB_TYPE)
                .user_id(UUID.randomUUID().toString())
                .build();
        String timestamp = AppUtil.getSmileIdTimestamp();
        SmileIdEnhancedKycRequest smileIdEnhancedKycRequest = SmileIdEnhancedKycRequest.newRequest(request.idNumber(), request.idType(), smileIdProperties.partnerId(), partnerParams, getSignature(timestamp), timestamp, request.country());

        return smileIdClient.enhancedBvnQuery(smileIdEnhancedKycRequest);
    }

    /**
     * Generates a Smile ID smart link for user verification.
     *
     * @param smileRequest The request containing job ID and requirement id for verification.
     * @return A {@link UpdateResponse} containing a successful message.
     * @throws BadRequestException      If the ID card already exists or the requirement is already completed.
     * @throws UpstreamServiceException If the token generation fails.
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public UpdateResponse saveIdTask(IdVerificationRequest smileRequest) {

        org.meristem.oneapp.usersservice.models.Vendor vendor = amlVendorRepository.findAmlVendorByVendorCode(Vendor.SMILE_ID.getValue());

        InvestmentRequirement requirements = requirementsRepository.findInvestmentRequirementsByRequirementName(OnboardingRequirements.BVN.getName(), EntityStatus.ACTIVE.getValue(), AppUtil.getInvestmentId(httpServletRequest)).orElseThrow(() -> new BadRequestException("Requirement not found"));
        IdQueryDetailsDto dto = new IdQueryDetailsDto();
        dto.setBvn(encryptionUtil.encrypt(smileRequest.idNumber()));
        dto.setBvnHashed(hashingUtil.hmacWithSha256(idHashKey, smileRequest.idNumber()));
        requireNonNull(cacheManager.getCache(AppConstants.SIGN_UP_CACHE_NAME)).put(dto.getBvnHashed(), dto);
        kycQueryRepository.findKycQueryByJobId(smileRequest.jobId()).ifPresentOrElse(k -> {
                },
                () -> kycQueryRepository.save(KycQuery.builder().jobId(smileRequest.jobId()).investmentRequirementId(requirements.getId()).userId(hashingUtil.hmacWithSha256(idHashKey, smileRequest.idNumber()))
                        .status(KycQueryStatus.PENDING.getValue()).vendorId(vendor.getId()).build()));
        return UpdateResponse.builder().message("Success").success(true).build();
    }

    /**
     * Handles Smile ID webhook notifications for verification results.
     *
     * @param request The webhook notification containing verification details.
     * @return A {@link SmileIdWebhookResponse} indicating the success or failure of the operation.
     * @throws BadRequestException If the request is invalid or the user is not found.
     */

    @Transactional
    public UpdateResponse handleWebhook(SmileIdWebhookNotification request) {
        SmileIdWebhookResponse response = new SmileIdWebhookResponse("Failed", false, null);

        String smileIdWebhookUrl = "/topic/smile-id/";
        try {
            KycQuery record = kycQueryRepository.findKycQueryByJobIdAndStatusIn(request.getPartnerParams().jobId(), List.of(KycQueryStatus.PENDING.getValue(), KycQueryStatus.COMPLETED.getValue()))
                    .orElseThrow(() -> new BadRequestException("Initial setup not found."));
            if (!confirmSignature(request.getSignature(), request.getTimestamp()) || !smileIps.contains(AppUtil.extractIp(httpServletRequest))) {
                return new UpdateResponse("Failed", false);
            }
            if (rejectionsStatus.contains(request.getResultCode())) {
                response = handleFailedNotification(request, record);
            } else if (actionStatus.contains(request.getResultCode()) || dataStatus.contains(request.getResultCode())) {
                response = handleSuccessfulNotification(request, record);
                WebSocketDto responseWebSocketDto = new WebSocketDto(smileIdWebhookUrl + request.getPartnerParams().jobId(), response);
                kafkaSenderService.send(responseWebSocketDto, Map.of(KafkaHeaders.TOPIC, KafkaTopics.KAFKA_SMILE_ID_TOPIC, KafkaHeaders.KEY, record.getJobId()));
            }
        } catch (RuntimeException e) {
            WebSocketDto responseWebSocketDto = new WebSocketDto(smileIdWebhookUrl + request.getPartnerParams().jobId(), response);
            kafkaSenderService.send(responseWebSocketDto, Map.of(KafkaHeaders.TOPIC, KafkaTopics.KAFKA_SMILE_ID_TOPIC, KafkaHeaders.KEY, request.getPartnerParams().jobId()));
            log.error(e.getMessage(), e);
            throw new BadRequestException("Bad request: invalid request");
        }
        WebSocketDto responseWebSocketDto = new WebSocketDto(smileIdWebhookUrl + request.getPartnerParams().jobId(), response);
        messagingTemplate.convertAndSend(responseWebSocketDto.url(), responseWebSocketDto.body());

        kafkaSenderService.send(responseWebSocketDto, Map.of(KafkaHeaders.TOPIC, KafkaTopics.KAFKA_SMILE_ID_TOPIC, KafkaHeaders.KEY, request.getPartnerParams().jobId()));

        return new UpdateResponse(response.message(), response.status());
    }

    /**
     * Handles failed webhook notifications by updating the Smile ID record and user onboarding status.
     *
     * @param notification The webhook notification containing failure details.
     * @param kycQuery     The Smile ID record associated with the notification.
     */
    private SmileIdWebhookResponse handleFailedNotification(SmileIdWebhookNotification notification, KycQuery kycQuery) {
        kycQuery.setMessage(notification.getResultText());
        kycQuery.setStatus(KycQueryStatus.FAILED.getValue());
        kycQueryRepository.save(kycQuery);
        return new SmileIdWebhookResponse("Failed", false, null);
    }

    /**
     * Handles successful webhook notifications by processing the action or data based on the job type.
     *
     * @param notification The webhook notification containing success details.
     * @param kycQuery     The Smile ID record associated with the notification.
     */
    private SmileIdWebhookResponse handleSuccessfulNotification(SmileIdWebhookNotification notification, KycQuery kycQuery) {

        if (actionStatus.contains(notification.getResultCode())) {
            return handleAction(notification, kycQuery);
        }
        return handleData(notification, kycQuery);
    }

    /**
     * Processes action-based webhook notifications and completes user onboarding.
     *
     * @param notification The webhook notification containing action details.
     * @param kycQuery     The Smile ID record associated with the notification.
     */
    private SmileIdWebhookResponse handleAction(SmileIdWebhookNotification notification, KycQuery kycQuery) {

        kycQuery.setMessage(notification.getResultText());
        kycQuery.setStatus(KycQueryStatus.COMPLETED.getValue());
        kycQueryRepository.save(kycQuery);
        return new SmileIdWebhookResponse("Success", true, null);
    }

    /**
     * Processes data-based webhook notifications and updates user documents or profiles.
     *
     * @param notification The webhook notification containing data details.
     * @param kycQuery     The Smile ID record associated with the notification.
     */
    private SmileIdWebhookResponse handleData(SmileIdWebhookNotification notification, KycQuery kycQuery) {
        requirementsRepository.findByIdAndStatus(kycQuery.getInvestmentRequirementId(), EntityStatus.ACTIVE.getValue())
                .orElseThrow(() -> new BadRequestException("Requirement not found"));

        Cache cache = cacheManager.getCache(AppConstants.SIGN_UP_CACHE_NAME);
        String hashedIdNumber = hashingUtil.hmacWithSha256(idHashKey, notification.getIdNumber());
        IdQueryDetailsDto dto = requireNonNull(cache, "Cache not found").get(hashedIdNumber, IdQueryDetailsDto.class);
        requireNonNull(dto, "item not found");

        if (OnboardingRequirements.of(notification.getIdType()) == OnboardingRequirements.BVN) {
            Optional<IdCard> idCard = idCardRepository.findByIdValueHashedAndIdCardType(hashedIdNumber, IdCardType.BVN.getName());
            idCard.ifPresent(card -> userProfileRepository.updateBvnVerified(card.getUserId(), true));
            dto = userIdDetailsMapper.smileIdBvnLookupResponseToIdQueryDetailsDto(notification);
            dto.setBvn(encryptionUtil.encrypt(notification.getIdNumber()));
            dto.setBvnHashed(hashedIdNumber);
            dto.setBvnFacialVerified(true);
            cache.put(hashedIdNumber, dto);

            BvnQueryResponse bvnQueryResponse = BvnQueryResponse.builder().middleName(MaskingUtils.maskMiddleName(dto.getMiddleName()))
                    .email(dto.getEmail()).firstName(MaskingUtils.maskFirstName(dto.getFirstName()))
                    .lastName(MaskingUtils.maskLastName(dto.getLastName()))
                    .phoneNumber(dto.getPhoneNumber())
                    .build();
            return new SmileIdWebhookResponse("Success", true, bvnQueryResponse);
        }
        return new SmileIdWebhookResponse("Success", true, null);
    }

    /**
     * Validates NIN data; flags mismatches; completes onboarding if valid
     */
    public IdValidationResponse validateNin(NinValidationRequest request) {

        Cache cache = getIdQueryCache(cacheManager);

        IdQueryRequest idQueryRequest = IdQueryRequest.builder()
                .idType("NIN_V2").idNumber(request.idNumber()).country("NG").build();
        SmileIdWebhookNotification notification = getSmileIdWebhookNotification(idQueryRequest, IdCardType.NIN, AppUtil.getLoggedInUserId());

        if (errorCodes.contains(notification.getResultCode())) {
            throw new ResourceNotFoundException("Invalid NIN", idQueryRequest.idType(), request.idNumber());
        }
        notification.setIdType(IdCardType.NIN.getName());
        Users loggedInUser = usersRepository.findById(AppUtil.getLoggedInUserId()).orElseThrow(() -> new AuthorizationDeniedException("User is not logged in"));
        UserIdDetails bvn = customRepository.findOneBy(UserIdDetails.class, Map.of("userId", loggedInUser.getId(), "idType", IdCardType.BVN.getName())).orElseThrow(() -> new BadRequestException("BVN details could not be found."));

        List<String> names = AppUtil.buildNames(bvn.getFirstName(), bvn.getMiddleName(), bvn.getLastName());

        UserIdDetails nin = idDetailsService.buildAndSaveIdDetails(userIdDetailsMapper.smileIdBvnLookupResponseToIdQueryDetailsDto(notification), loggedInUser);
        return compareNinAndBvnDetailsSaveAndReturn(cache, nin, names, bvn, loggedInUser, requirementsRepository, usersRepository, userOnboardingRepository, usersService, customRepository, idCardRepository, encryptionUtil.encrypt(request.idNumber()), hashingUtil.hmacWithSha256(idHashKey, request.idNumber()), AppUtil.getInvestmentId(httpServletRequest));

    }

    @Override
    public IdQueryDetailsDto ninQuery(String nin) {
        IdQueryRequest request = IdQueryRequest.builder().country("NG").idType("NIN_V2").idNumber(nin).build();
        SmileIdWebhookNotification response = getSmileIdWebhookNotification(request, IdCardType.NIN, null);
        if (ID_APPROVED_STATUS.equals(response.getResultCode()) && confirmSignature(response.getSignature(), response.getTimestamp())) {

            return IdQueryDetailsDto.builder().middleName(response.getMiddleName())
                    .email(response.getEmail()).firstName(response.getFirstName())
                    .lastName(response.getLastName()).idType(IdCardType.NIN.getName())
                    .phoneNumber(response.getPhoneNumber())
                    .dateOfBirth(response.getDateOfBirth())
                    .localAreaOfOrigin(response.getLocalAreaOfOrigin())
                    .gender(response.getGender())
                    .build();
        } else if (errorCodes.contains(response.getResultCode())) {
            throw new ResourceNotFoundException("Enter a valid bvn", request.idType(), request.idNumber());
        } else {
            throw new BadRequestException("Try again later.");
        }
    }

    /**
     * Confirms the validity of a received signature by comparing it with a generated signature.
     *
     * @param receivedSignature The signature received in the request.
     * @param receivedTimestamp The timestamp received in the request.
     * @return True if the signature is valid, false otherwise.
     */
    private boolean confirmSignature(String receivedSignature, String receivedTimestamp) {

        try {
            Mac mac = getMac(receivedTimestamp);
            String generatedSignature = Base64.getEncoder().encodeToString(mac.doFinal());
            return generatedSignature.equals(receivedSignature);
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    /**
     * Creates and initializes an HMAC-SHA256 {@link Mac} instance for signature generation.
     *
     * @param timestamp The timestamp to include in the MAC initialization.
     * @return The initialized {@link Mac} instance.
     * @throws NoSuchAlgorithmException If the HMAC-SHA256 algorithm is not available.
     * @throws InvalidKeyException      If the provided key is invalid.
     */
    private Mac getMac(String timestamp) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac mac = AppUtil.getHmacSHA256();
        mac.init(new SecretKeySpec(smileIdProperties.apiKey().getBytes(), "HmacSHA256"));
        mac.update(timestamp.getBytes(StandardCharsets.UTF_8));
        mac.update(smileIdProperties.partnerId().getBytes(StandardCharsets.UTF_8));
        mac.update("sid_request".getBytes(StandardCharsets.UTF_8));
        return mac;
    }

    private String getSignature(String timestamp) {

        try {
            return Base64.getEncoder().encodeToString(getMac(timestamp).doFinal());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new BadRequestException("Bad request: invalid request");
        }
    }

    @Transactional
    public UpdateResponse saveIdTask(@Valid WebIdVerificationRequest smileRequest) {

        String sidServer = "prod".equalsIgnoreCase(profile) ? "1" : "0";  // Use '0' for the sandbox server

        Map<String, Object> optionalInfo = new HashMap<>();
//        optionalInfo.put("product_id", 1);

        if (!"prod".equalsIgnoreCase(profile)) {
            optionalInfo.put("sandbox_result", smileRequest.getPartnerParams().getSandboxResult());
        }
        WebApi connection = new WebApi(smileIdProperties.partnerId(), smileIdProperties.apiKey(), smileIdProperties.callbackUrl(), sidServer);
        String jobId = UUID.randomUUID().toString();
        String userId = UUID.randomUUID().toString();
        PartnerParams params = new PartnerParams(JobType.fromValue(smileRequest.getPartnerParams().getJobType()), userId, jobId, optionalInfo);
        List<ImageDetail> imageDetails = new ArrayList<>();
        smileRequest.getImages().forEach(image -> {
            imageDetails.add(new ImageDetail(ImageType.fromValue(image.imageTypeId()), image.image(), null));
        });
        boolean returnJobStatus = false; // Set to true if you want to get
        boolean returnHistory = false; // Set to true to receive all of the
        boolean returnImageLinks = false; // Set to true to receive links to

        Options options = new Options(returnHistory, returnImageLinks, returnJobStatus, smileIdProperties.callbackUrl());

        IdInfo idInfo = new IdInfo(null, null, null, "NG", IdCardType.BVN.getName(), smileRequest.getBvn(), null, null);
        try {
            JobStatusResponse response = connection.submitJob(params, imageDetails, idInfo, options);
            if (response.isJobSuccess()) {
            saveIdTask(IdVerificationRequest.builder().jobId(jobId).idNumber(smileRequest.getBvn()).investmentRequirementId(smileRequest.getInvestmentRequirementId()).build());
                return new UpdateResponse(jobId, true);
            }
            return new UpdateResponse("Failed", false);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
