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
import org.meristem.oneapp.usersservice.domains.requests.BvnQueryRequest;
import org.meristem.oneapp.usersservice.domains.requests.SmileIdIdRequest;
import org.meristem.oneapp.usersservice.domains.responses.BvnQueryResponse;
import org.meristem.oneapp.usersservice.domains.responses.SmileIdWebhookNotification;
import org.meristem.oneapp.usersservice.domains.responses.SmileIdWebhookResponse;
import org.meristem.oneapp.usersservice.domains.responses.UpdateResponse;
import org.meristem.oneapp.usersservice.exception.exceptions.BadRequestException;
import org.meristem.oneapp.usersservice.exception.exceptions.UpstreamServiceException;
import org.meristem.oneapp.usersservice.integrations.SmileIdClient;
import org.meristem.oneapp.usersservice.integrations.requests.SmileIdEnhancedKycRequest;
import org.meristem.oneapp.usersservice.integrations.responses.SmileIdEnhancedKycResponse;
import org.meristem.oneapp.usersservice.mappers.UserIdDetailsMapper;
import org.meristem.oneapp.usersservice.models.*;
import org.meristem.oneapp.usersservice.repositories.*;
import org.meristem.oneapp.usersservice.services.IKafkaSenderService;
import org.meristem.oneapp.usersservice.services.ISmileIdService;
import org.meristem.oneapp.usersservice.services.IUsersService;
import org.meristem.oneapp.usersservice.utils.AppUtil;
import org.meristem.oneapp.usersservice.utils.HashingUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.kafka.support.KafkaHeaders;
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
@RequiredArgsConstructor
@Service
public class SmileIdService implements ISmileIdService {

    public static final String ID_APPROVED_STATUS = "1012";
    @Value("${one-app.users-service.smile-id.server-ips}")
    private List<String> smileIps;

    @Value("${one-app.users-service.smile-id.partner-id}")
    private String partnerId;

    @Value("${hashing.id-hash-key}")
    private String idHashKey;

    private static final String DOCUMENT_APPROVED_STATUS = "0810";
    private static final Integer DOCUMENT_JOB_TYPE = 6;
    private static final Integer ENHANCED_JOB_TYPE = 5;
    private static final List<Integer> DOC_AND_ENHANCED_JOB_TYPES = List.of(DOCUMENT_JOB_TYPE, ENHANCED_JOB_TYPE);

    private final SmileIdRecordRepository smileIdRecordRepository;
    private final UserOnboardingRepository userOnboardingRepository;
    private final RequirementsRepository requirementsRepository;
    private final UserDocumentRepository userDocumentRepository;
    private final IdCardRepository idCardRepository;
    private final UserProfileRepository userProfileRepository;
    private final UsersRepository usersRepository;
    private final CacheManager cacheManager;
    private final IUsersService usersService;
    private final UserIdDetailsMapper userIdDetailsMapper = UserIdDetailsMapper.INSTANCE;
    private final CustomRepository customRepository;
    private final HttpServletRequest httpServletRequest;
    private final IKafkaSenderService kafkaSenderService;
    private final SmileIdClient smileIdClient;
    private final SmileIdProperties smileIdProperties;
    private final HashingUtil hashingUtil;

    List<String> dataStatus = List.of(ID_APPROVED_STATUS, DOCUMENT_APPROVED_STATUS);
    List<String> actionStatus = List.of("1210", DOCUMENT_APPROVED_STATUS);
    List<String> errorCodes = List.of("1013", "1014");

    List<String> rejectionsStatus = List.of("1211", "1212", "1213", "0911", "0912", "0811", "0813", "0811", "0812", "1014");



    public BvnQueryResponse bvnQuery(BvnQueryRequest request) {

        if (idCardRepository.existsByIdValueHashed(hashingUtil.hmacWithSha256(idHashKey, request.bvn()))) {
            throw new BadRequestException("Bvn already exists.");
        }
        SmileIdEnhancedKycRequest.PartnerParams  partnerParams = SmileIdEnhancedKycRequest.PartnerParams.builder()
                .job_id(UUID.randomUUID().toString())
                .job_type(ENHANCED_JOB_TYPE)
                .user_id(UUID.randomUUID().toString())
                .build();
        String timestamp = AppUtil.getSmileIdTimestamp();
        SmileIdEnhancedKycRequest smileIdEnhancedKycRequest = SmileIdEnhancedKycRequest.newRequest(request.bvn(), request.idType(), partnerId, partnerParams, getSignature(timestamp), timestamp, request.country());

        SmileIdEnhancedKycResponse response = smileIdClient.enhancedBvnQuery(smileIdEnhancedKycRequest);

        if (ID_APPROVED_STATUS.equals(response.resultCode()) && confirmSignature(response.signature(), response.timestamp())) {

            return BvnQueryResponse.builder()
                    .email(response.email()).firstName(response.firstName()).lastName(response.lastName())
                    .phoneNumber(response.phoneNumber()).build();
        } else if (errorCodes.contains(response.resultCode())) {
            throw new BadRequestException("Enter a valid bvn");
        } else {
            throw new BadRequestException("Try again later.");
        }
    }

    /**
     * Generates a Smile ID smart link for user verification.
     *
     * @param smileRequest The request containing job ID and requirement id for verification.
     * @return A {@link UpdateResponse} containing a successful message.
     * @throws BadRequestException If the ID card already exists or the requirement is already completed.
     * @throws UpstreamServiceException If the token generation fails.
     */
    @Transactional
    public UpdateResponse saveSmileIdTask(SmileIdIdRequest smileRequest) {

        Requirements requirements = requirementsRepository.findByIdAndStatus(smileRequest.requirementId(), EntityStatus.ACTIVE.getValue())
                .orElseThrow(() -> new BadRequestException("Requirement not found"));

        smileIdRecordRepository.save(SmileIdRecord.builder().jobId(smileRequest.jobId()).requirementId(requirements.getId()).userId(AppUtil.getLoggedInUserEmail())
                .build());
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
            SmileIdRecord record = smileIdRecordRepository.findSmileIdRecordByJobId(request.partnerParams().jobId());
            if (!confirmSignature(request.signature(), request.timestamp()) || !smileIps.contains(AppUtil.extractIp(httpServletRequest))) {
                return new SmileIdWebhookResponse("Failed", false);
            }
            if (rejectionsStatus.contains(request.resultCode())) {
                handleFailedNotification(request, record);
            } else if (actionStatus.contains(request.resultCode()) || dataStatus.contains(request.resultCode())) {
                handleSuccessfulNotification(request, record);
                response = new SmileIdWebhookResponse("Success", true);
                WebSocketDto responseWebSocketDto = new WebSocketDto(smileIdWebhookUrl + request.partnerParams().jobId(), response);
                kafkaSenderService.send(responseWebSocketDto, Map.of(KafkaHeaders.TOPIC, KafkaTopics.KAFKA_SMILE_ID_TOPIC, KafkaHeaders.KEY, record.getJobId()));
                return response;
            }
        } catch (RuntimeException e) {
            WebSocketDto responseWebSocketDto = new WebSocketDto(smileIdWebhookUrl + request.partnerParams().jobId(), response);
            kafkaSenderService.send(responseWebSocketDto, Map.of(KafkaHeaders.TOPIC, KafkaTopics.KAFKA_SMILE_ID_TOPIC, KafkaHeaders.KEY, request.partnerParams().jobId()));
            log.error(e.getMessage(), e);
            throw new BadRequestException("Bad request: invalid request");
        }
        WebSocketDto responseWebSocketDto = new WebSocketDto(smileIdWebhookUrl + request.partnerParams().jobId(), response);
        kafkaSenderService.send(responseWebSocketDto, Map.of(KafkaHeaders.TOPIC, KafkaTopics.KAFKA_SMILE_ID_TOPIC, KafkaHeaders.KEY, request.partnerParams().jobId()));

        return response;
    }

    /**
     * Handles failed webhook notifications by updating the Smile ID record and user onboarding status.
     *
     * @param notification The webhook notification containing failure details.
     * @param smileIdRecord The Smile ID record associated with the notification.
     */
    private void handleFailedNotification(SmileIdWebhookNotification notification, SmileIdRecord smileIdRecord) {
        Users loggedInUser = usersRepository.findOneByEmail(smileIdRecord.getUserId()).orElseThrow(() -> new BadRequestException("User not found"));
        smileIdRecord.setMessage(notification.resultText());
        smileIdRecord.setStatus(SmileIdRecordStatus.FAILED.getValue());
        userOnboardingRepository.updateUserOnboardingStatus(loggedInUser.getId(), smileIdRecord.getRequirementId(), OnboardingStatus.REJECTED.getValue(), false);
        smileIdRecordRepository.save(smileIdRecord);
    }

    /**
     * Handles successful webhook notifications by processing the action or data based on the job type.
     *
     * @param notification The webhook notification containing success details.
     * @param smileIdRecord The Smile ID record associated with the notification.
     */
    private void handleSuccessfulNotification(SmileIdWebhookNotification notification, SmileIdRecord smileIdRecord) {

        Users loggedInUser = usersRepository.findOneByEmail(smileIdRecord.getUserId()).orElseThrow(() -> new BadRequestException("User not found"));

        if (actionStatus.contains(notification.resultCode())) {
            handleAction(notification, smileIdRecord);
        } else if (dataStatus.contains(notification.resultCode()) && DOC_AND_ENHANCED_JOB_TYPES.contains(notification.partnerParams().jobType())) {
            saveUserIdDetails(notification, smileIdRecord, loggedInUser);
        } else if (dataStatus.contains(notification.resultCode())){
            handleData(notification, smileIdRecord);
        }
    }

    private void saveUserIdDetails(SmileIdWebhookNotification notification, SmileIdRecord smileIdRecord, Users loggedInUser) {


        if (StringUtils.isNotBlank(notification.expirationDate()) && notification.expirationDate().matches(AppConstants.DATE_REGEX)) {

            if (LocalDate.parse(notification.expirationDate()).isBefore(LocalDate.now())) {
                usersService.resetUserOnboarding(loggedInUser.getEmail(), smileIdRecord.getRequirementId());
                return;
            }
        }

        customRepository.findOneBy(UserIdDetails.class, Map.of("userId", loggedInUser.getId(), "idType", IdCardType.fromName(notification.idType()).getName(), "idNumber", notification.idNumber()))
                .ifPresentOrElse(u -> {
                }, () -> {
                    UserIdDetails userIdDetails = userIdDetailsMapper.smileIdWebhookNotificationToUserIdDetails(notification);
                    userIdDetails.setIdType(IdCardType.fromName(notification.idType()).getName());
                    userIdDetails.setGender(Gender.getGender(notification.gender()).getCaps());

                    userIdDetails.setUserId(loggedInUser.getId());
                    customRepository.save(userIdDetails);
                });

        completeOnboarding(smileIdRecord, loggedInUser);
    }

    /**
     * Processes action-based webhook notifications and completes user onboarding.
     *
     * @param notification The webhook notification containing action details.
     * @param smileIdRecord The Smile ID record associated with the notification.
     */
    private void handleAction(SmileIdWebhookNotification notification, SmileIdRecord smileIdRecord) {

        Users loggedInUser = usersRepository.findOneByEmail(smileIdRecord.getUserId()).orElseThrow(() -> new BadRequestException("User not found"));

        if (DOCUMENT_JOB_TYPE.equals(notification.partnerParams().jobType())) {
            saveUserIdDetails(notification, smileIdRecord, loggedInUser);
        }
        smileIdRecord.setMessage(notification.resultText());
        smileIdRecord.setStatus(SmileIdRecordStatus.APPROVED.getValue());
        smileIdRecordRepository.save(smileIdRecord);
        completeOnboarding(smileIdRecord, loggedInUser);
    }

    /**
     * Processes data-based webhook notifications and updates user documents or profiles.
     *
     * @param notification The webhook notification containing data details.
     * @param smileIdRecord The Smile ID record associated with the notification.
     */
    private void handleData(SmileIdWebhookNotification notification, SmileIdRecord smileIdRecord) {
        requirementsRepository.findByIdAndStatus(smileIdRecord.getRequirementId(), EntityStatus.ACTIVE.getValue())
                .orElseThrow(() -> new BadRequestException("Requirement not found"));

        // Save document url for non bvn requirement
        Users loggedInUser = usersRepository.findOneByEmail(smileIdRecord.getUserId()).orElseThrow(() -> new BadRequestException("User not found"));

        userDocumentRepository.findByUserIdAndIdType(loggedInUser.getId(), notification.idType())
                .ifPresentOrElse(id -> {
                    id.setAdditionalUrl(notification.kycReceipt());
                    if (nonNull(notification.imageLinks())) {
                        id.setIdCardFront(notification.imageLinks().idCardImage());
                        id.setIdCardBack(notification.imageLinks().idCardBack());
                        id.setSelfieImage(notification.imageLinks().selfieImage());
                    }
                    userDocumentRepository.save(id);
                }, () -> {
                    UserDocument document = UserDocument.builder().userId(loggedInUser.getId()).requirementId(smileIdRecord.getRequirementId())
                            .idType(notification.idType()).additionalUrl(notification.kycReceipt()).build();
                    if (nonNull(notification.imageLinks())) {
                        document.setIdCardFront(notification.imageLinks().idCardImage());
                        document.setIdCardBack(notification.imageLinks().idCardBack());
                        document.setSelfieImage(notification.imageLinks().selfieImage());
                    }
                    userDocumentRepository.save(document);
                });


        if (AppUtil.nonIsNull(notification.idNumber(), notification.idType())) {
            idCardRepository.findByIdCardTypeAndIdValue(IdCardType.fromName(notification.idType()).getName(), notification.idNumber())
                    .ifPresentOrElse(id -> {
                    }, () -> idCardRepository.save(IdCard.builder().idValue(notification.idNumber())
                            .idCardType(notification.idType())
                            // "yyyy-MM-dd"
                            .expiryDate(StringUtils.isNotBlank(notification.expirationDate()) ? LocalDate.parse(notification.expirationDate()) : null)
                            .issuedDate(StringUtils.isNotBlank(notification.issuanceDate()) ? LocalDate.parse(notification.issuanceDate()) : null)
                            .userId(loggedInUser.getId())
                            .build()));
        }

        if (OnboardingRequirements.of(notification.idType()) == OnboardingRequirements.BVN) {

            UserProfile profile = userProfileRepository.findByUserId(loggedInUser.getId()).orElseThrow(() -> new BadRequestException("User not found"));

            profile.setGender(Gender.getGender(notification.gender()).getCaps());
            profile.setDateOfBirth(LocalDate.parse(notification.dob()));
            profile.setCountryOfOrigin(getCountry(notification));
            profile.setLgOfOrigin(getLgo(notification));
            profile.setStateOfOrigin(notification.placeOfBirth());
            userProfileRepository.save(profile);

            requireNonNull(cacheManager.getCache(AppConstants.USERS_CACHE_NAME)).evict(loggedInUser.getId());
        }
    }

    /**
     * Completes the onboarding process for a user by updating the onboarding status and notifying the user service.
     *
     * @param smileIdRecord The Smile ID record associated with the onboarding process.
     * @param loggedInUser The user completing the onboarding process.
     */
    private void completeOnboarding(SmileIdRecord smileIdRecord, Users loggedInUser) {
        userOnboardingRepository.updateUserOnboardingStatus(loggedInUser.getId(), smileIdRecord.getRequirementId(), OnboardingStatus.APPROVED.getValue(), true);
        usersService.completeUserOnboarding(loggedInUser.getEmail());
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
     * @throws InvalidKeyException If the provided key is invalid.
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

        if (StringUtils.isNotBlank(request.nationality())) {
            return request.nationality();
        } else if (StringUtils.isNotBlank(request.countryOfBirth())) {
            return request.countryOfBirth();
        }
        return Country.getCountry(request.country()).getCountryName();
    }

    /**
     * Retrieves the local government area of origin from the webhook notification.
     *
     * @param request The webhook notification containing LGA details.
     * @return The local government area of origin as a string.
     */
    private String getLgo(SmileIdWebhookNotification request) {
        if (StringUtils.isNotBlank(request.localAreaOfOrigin())) {
            return request.localAreaOfOrigin();
        }  else  {
            return request.regionOfOrigin();
        }
    }
}
