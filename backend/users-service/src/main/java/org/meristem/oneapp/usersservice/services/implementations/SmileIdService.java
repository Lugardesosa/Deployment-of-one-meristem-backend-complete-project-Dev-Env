package org.meristem.oneapp.usersservice.services.implementations;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.meristem.oneapp.kafka.dtos.WebSocketDto;
import org.meristem.oneapp.usersservice.config.configProperties.SmileIdProperties;
import org.meristem.oneapp.usersservice.constants.AppConstants;
import org.meristem.oneapp.usersservice.constants.KafkaTopics;
import org.meristem.oneapp.usersservice.domains.enums.*;
import org.meristem.oneapp.usersservice.domains.enums.Vendor;
import org.meristem.oneapp.usersservice.domains.requests.IdQueryRequest;
import org.meristem.oneapp.usersservice.domains.requests.IdVerificationRequest;
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
import org.springframework.cache.CacheManager;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
@RequiredArgsConstructor
@Service("SMILE_ID")
@Transactional
public class SmileIdService implements IKycService {

    public static final String ID_APPROVED_STATUS = "1012";
    private final CustomRepository customRepository;
    private final SmileIdProperties smileIdProperties;
    private final AmlVendorRepository amlVendorRepository;
    private final UserIdDetailsMapper userIdDetailsMapper = UserIdDetailsMapper.INSTANCE;
    @Value("${one-app.users-service.smile-id.server-ips}")
    private List<String> smileIps;

    @Value("${one-app.users-service.smile-id.partner-id}")
    private String partnerId;

    @Value("${hashing.id-hash-key}")
    private String idHashKey;

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
    private final IIdDetailsService  idDetailsService;

    List<String> dataStatus = List.of(ID_APPROVED_STATUS, DOCUMENT_APPROVED_STATUS);
    List<String> actionStatus = List.of("1210", DOCUMENT_APPROVED_STATUS);
    List<String> errorCodes = List.of("1013", "1014");

    List<String> rejectionsStatus = List.of("1211", "1212", "1213", "0911", "0912", "0811", "0813", "0811", "0812", "1014");

    public BvnQueryResponse bvnQuery(IdQueryRequest request) {

        if (IdCardType.BVN.compareTo(IdCardType.fromName(request.idType())) != 0) {
            throw new BadRequestException("Only BVN can be validated.");
        }

        SmileIdWebhookNotification response = getSmileIdWebhookNotification(request, IdCardType.BVN);
        if (ID_APPROVED_STATUS.equals(response.getResultCode()) && confirmSignature(response.getSignature(), response.getTimestamp())) {

            IdQueryDetailsDto dto = userIdDetailsMapper.smileIdBvnLookupResponseToIdQueryDetailsDto(response);
            return getBvnQueryResponse(cacheManager, request, dto, encryptionUtil, hashingUtil, idHashKey);
        } else if (errorCodes.contains(response.getResultCode())) {
            throw new ResourceNotFoundException("Enter a valid bvn", request.idType(), request.idNumber());
        } else {
            throw new BadRequestException("Try again later.");
        }
    }

    private SmileIdWebhookNotification getSmileIdWebhookNotification(IdQueryRequest request, IdCardType idCardType) {
        if (idCardRepository.existsByIdValueHashedAndIdCardType(hashingUtil.hmacWithSha256(idHashKey, request.idNumber()), idCardType.getName())) {
            throw new BadRequestException(idCardType.getName() + " already exists.");
        }
        SmileIdEnhancedKycRequest.PartnerParams partnerParams = SmileIdEnhancedKycRequest.PartnerParams.builder()
                .job_id(UUID.randomUUID().toString())
                .job_type(ENHANCED_JOB_TYPE)
                .user_id(UUID.randomUUID().toString())
                .build();
        String timestamp = AppUtil.getSmileIdTimestamp();
        SmileIdEnhancedKycRequest smileIdEnhancedKycRequest = SmileIdEnhancedKycRequest.newRequest(request.idNumber(), request.idType(), partnerId, partnerParams, getSignature(timestamp), timestamp, request.country());

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
    @Transactional
    public UpdateResponse saveIdTask(IdVerificationRequest smileRequest) {

        org.meristem.oneapp.usersservice.models.Vendor vendor = amlVendorRepository.findAmlVendorByVendorCode(Vendor.SMILE_ID.getValue());
        Requirements requirements = requirementsRepository.findByIdAndStatus(smileRequest.requirementId(), EntityStatus.ACTIVE.getValue())
                .orElseThrow(() -> new BadRequestException("Requirement not found"));
        userOnboardingRepository.updateUserOnboardingStatus(AppUtil.getLoggedInUserId(), requirements.getId(), OnboardingStatus.PENDING.getValue(), UserOnboardingNotes.APPROVED.note, false);
        kycQueryRepository.save(KycQuery.builder().jobId(smileRequest.jobId()).requirementId(requirements.getId()).userId(AppUtil.getLoggedInUserEmail())
                .status(KycQueryStatus.PENDING.getValue()).vendorId(vendor.getId()).build());
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
    public SmileIdWebhookResponse handleWebhook(SmileIdWebhookNotification request) {
        SmileIdWebhookResponse response = new SmileIdWebhookResponse("Failed", false);

        String smileIdWebhookUrl = "/topic/smile-id/";
        try {
            KycQuery record = kycQueryRepository.findKycQueryByJobIdAndStatusIn(request.getPartnerParams().jobId(), List.of(KycQueryStatus.PENDING.getValue(), KycQueryStatus.COMPLETED.getValue()))
                    .orElseThrow(() -> new BadRequestException("Initial setup not found."));
            if (!confirmSignature(request.getSignature(), request.getTimestamp()) || !smileIps.contains(AppUtil.extractIp(httpServletRequest))) {
                return new SmileIdWebhookResponse("Failed", false);
            }
            if (rejectionsStatus.contains(request.getResultCode())) {
                handleFailedNotification(request, record);
            } else if (actionStatus.contains(request.getResultCode()) || dataStatus.contains(request.getResultCode())) {
                handleSuccessfulNotification(request, record);
                response = new SmileIdWebhookResponse("Success", true);
                WebSocketDto responseWebSocketDto = new WebSocketDto(smileIdWebhookUrl + request.getPartnerParams().jobId(), response);
                kafkaSenderService.send(responseWebSocketDto, Map.of(KafkaHeaders.TOPIC, KafkaTopics.KAFKA_SMILE_ID_TOPIC, KafkaHeaders.KEY, record.getJobId()));
                return response;
            }
        } catch (RuntimeException e) {
            WebSocketDto responseWebSocketDto = new WebSocketDto(smileIdWebhookUrl + request.getPartnerParams().jobId(), response);
            kafkaSenderService.send(responseWebSocketDto, Map.of(KafkaHeaders.TOPIC, KafkaTopics.KAFKA_SMILE_ID_TOPIC, KafkaHeaders.KEY, request.getPartnerParams().jobId()));
            log.error(e.getMessage(), e);
            throw new BadRequestException("Bad request: invalid request");
        }
        WebSocketDto responseWebSocketDto = new WebSocketDto(smileIdWebhookUrl + request.getPartnerParams().jobId(), response);
        kafkaSenderService.send(responseWebSocketDto, Map.of(KafkaHeaders.TOPIC, KafkaTopics.KAFKA_SMILE_ID_TOPIC, KafkaHeaders.KEY, request.getPartnerParams().jobId()));

        return response;
    }

    /**
     * Handles failed webhook notifications by updating the Smile ID record and user onboarding status.
     *
     * @param notification  The webhook notification containing failure details.
     * @param kycQuery The Smile ID record associated with the notification.
     */
    private void handleFailedNotification(SmileIdWebhookNotification notification, KycQuery kycQuery) {
        Users loggedInUser = usersRepository.findOneByEmail(kycQuery.getUserId()).orElseThrow(() -> new BadRequestException("User not found"));
        kycQuery.setMessage(notification.getResultText());
        kycQuery.setStatus(KycQueryStatus.FAILED.getValue());
        usersService.resetUserOnboarding(loggedInUser.getEmail(), kycQuery.getRequirementId());
        kycQueryRepository.save(kycQuery);
    }

    /**
     * Handles successful webhook notifications by processing the action or data based on the job type.
     *
     * @param notification  The webhook notification containing success details.
     * @param kycQuery The Smile ID record associated with the notification.
     */
    private void handleSuccessfulNotification(SmileIdWebhookNotification notification, KycQuery kycQuery) {

        if (actionStatus.contains(notification.getResultCode())) {
            handleAction(notification, kycQuery);
        } else if (dataStatus.contains(notification.getResultCode())) {
            handleData(notification, kycQuery);
        }
    }

    /**
     * Processes action-based webhook notifications and completes user onboarding.
     *
     * @param notification  The webhook notification containing action details.
     * @param kycQuery The Smile ID record associated with the notification.
     */
    private void handleAction(SmileIdWebhookNotification notification, KycQuery kycQuery) {

        Users loggedInUser = usersRepository.findOneByEmail(kycQuery.getUserId()).orElseThrow(() -> new BadRequestException("User not found"));

        kycQuery.setMessage(notification.getResultText());
        kycQuery.setStatus(KycQueryStatus.COMPLETED.getValue());
        kycQueryRepository.save(kycQuery);
        userOnboardingRepository.updateUserOnboardingStatus(loggedInUser.getId(), kycQuery.getRequirementId(), OnboardingStatus.APPROVED.getValue(), UserOnboardingNotes.APPROVED.note, true);
        usersService.completeUserOnboarding(loggedInUser.getEmail());

    }

    /**
     * Processes data-based webhook notifications and updates user documents or profiles.
     *
     * @param notification  The webhook notification containing data details.
     * @param kycQuery The Smile ID record associated with the notification.
     */
    private void handleData(SmileIdWebhookNotification notification, KycQuery kycQuery) {
        requirementsRepository.findByIdAndStatus(kycQuery.getRequirementId(), EntityStatus.ACTIVE.getValue())
                .orElseThrow(() -> new BadRequestException("Requirement not found"));

        // Save document url for non bvn requirement
        Users loggedInUser = usersRepository.findOneByEmail(kycQuery.getUserId()).orElseThrow(() -> new BadRequestException("User not found"));
        // Updates user profile with BVN data; evicts cache
        if (OnboardingRequirements.of(notification.getIdType()) == OnboardingRequirements.BVN) {

            UserProfile profile = userProfileRepository.findByUserId(loggedInUser.getId()).orElseThrow(() -> new BadRequestException("User not found"));

            profile.setGender(Gender.getGender(notification.getGender()).getCaps());
            profile.setDateOfBirth(LocalDate.parse(notification.getDateOfBirth()));
            profile.setCountryOfOrigin(getCountry(notification));
            profile.setLgOfOrigin(getLgo(notification));
            profile.setStateOfOrigin(notification.getPlaceOfBirth());
            userProfileRepository.save(profile);

            requireNonNull(cacheManager.getCache(AppConstants.USERS_CACHE_NAME)).evict(loggedInUser.getId());
        }

        idDetailsService.buildAndSaveIdDetails(userIdDetailsMapper.smileIdBvnLookupResponseToIdQueryDetailsDto(notification), loggedInUser);
    }

    /**
     * Validates NIN data; flags mismatches; completes onboarding if valid
     */
    public NinValidationResponse validateNin(IdQueryRequest request) {

        if (IdCardType.NIN.compareTo(IdCardType.fromName(request.idType())) != 0) {
            throw new BadRequestException("Only NIN can be validated.");
        }

        SmileIdWebhookNotification notification = getSmileIdWebhookNotification(request, IdCardType.NIN);

        if (errorCodes.contains(notification.getResultCode())) {
            throw new ResourceNotFoundException("Invalid NIN", request.idType(), request.idNumber());
        }
        Users loggedInUser = usersRepository.findById(AppUtil.getLoggedInUserId()).orElseThrow(() -> new AuthorizationDeniedException("User is not logged in"));
        UserIdDetails bvn = customRepository.findOneBy(UserIdDetails.class, Map.of("userId", loggedInUser.getId(), "idType", IdCardType.BVN.getName())).orElseThrow(() -> new BadRequestException("BVN details could not be found."));

        List<String> names = buildNames(bvn);

        UserIdDetails nin = idDetailsService.buildAndSaveIdDetails(userIdDetailsMapper.smileIdBvnLookupResponseToIdQueryDetailsDto(notification), loggedInUser);
        return compareNinAndBvnDetailsSaveAndReturn(nin, names, bvn, loggedInUser, requirementsRepository, userOnboardingRepository, usersService, customRepository);

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

    /**
     * Retrieves the country of origin from the webhook notification.
     *
     * @param request The webhook notification containing country details.
     * @return The country of origin as a string.
     */
    private String getCountry(SmileIdWebhookNotification request) {

        if (StringUtils.isNotBlank(request.getNationality())) {
            return request.getNationality();
        } else if (StringUtils.isNotBlank(request.getCountryOfBirth())) {
            return request.getCountryOfBirth();
        }
        return Country.getCountry(request.getCountry()).getCountryName();
    }

    /**
     * Retrieves the local government area of origin from the webhook notification.
     *
     * @param request The webhook notification containing LGA details.
     * @return The local government area of origin as a string.
     */
    private String getLgo(SmileIdWebhookNotification request) {
        if (StringUtils.isNotBlank(request.getLocalAreaOfOrigin())) {
            return request.getLocalAreaOfOrigin();
        } else {
            return request.getRegionOfOrigin();
        }
    }
}
