package org.meristem.oneapp.usersservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.meristem.oneapp.usersservice.config.configProperties.OneAppUsersProperties;
import org.meristem.oneapp.usersservice.config.configProperties.SmileIdProperties;
import org.meristem.oneapp.usersservice.constants.AppConstants;
import org.meristem.oneapp.usersservice.domains.enums.*;
import org.meristem.oneapp.usersservice.domains.requests.SmileIdIdTypeRequest;
import org.meristem.oneapp.usersservice.domains.responses.SmileIdWebhookNotification;
import org.meristem.oneapp.usersservice.domains.responses.SmileIdTokenResponse;
import org.meristem.oneapp.usersservice.domains.responses.SmileIdWebhookResponse;
import org.meristem.oneapp.usersservice.exception.exceptions.BadRequestException;
import org.meristem.oneapp.usersservice.exception.exceptions.UpstreamServiceException;
import org.meristem.oneapp.usersservice.integrations.SmileIdClient;
import org.meristem.oneapp.usersservice.integrations.requests.SmileIdSmileLinkRequest;
import org.meristem.oneapp.usersservice.integrations.responses.SmileIdSmileLinkResponse;
import org.meristem.oneapp.usersservice.mappers.UserIdDetailsMapper;
import org.meristem.oneapp.usersservice.models.*;
import org.meristem.oneapp.usersservice.repositories.*;
import org.meristem.oneapp.usersservice.utils.AppUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import static java.util.Objects.*;


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
public class SmileIdService {

    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    private static final String DOCUMENT_APPROVED_STATUS = "0810";
    private static final Integer DOCUMENT_JOB_TYPE = 6;
    private static final Integer ENHANCED_JOB_TYPE = 5;
    private static final List<Integer> DOC_AND_ENHANCED_JOB_TYPES = List.of(DOCUMENT_JOB_TYPE, ENHANCED_JOB_TYPE);

    @Value("${call.smile-id:true}")
    private boolean callSmileId;
    private final SmileIdRecordRepository smileIdRecordRepository;
    private final UserOnboardingRepository userOnboardingRepository;
    private final RequirementsRepository requirementsRepository;
    private final UserDocumentRepository userDocumentRepository;
    private final IdCardRepository idCardRepository;
    private final UserProfileRepository userProfileRepository;
    private final UsersRepository usersRepository;
    private final CacheManager cacheManager;
    private final SimpMessagingTemplate messagingTemplate;
    private final SmileIdClient smileIdClient;
    private final UsersService usersService;
    private final UserIdDetailsMapper userIdDetailsMapper = UserIdDetailsMapper.INSTANCE;
    private final CustomRepository customRepository;


    private final SmileIdProperties smileIdProperties;
    private final OneAppUsersProperties oneAppUsersProperties;

    List<String> dataStatus = List.of("1012", DOCUMENT_APPROVED_STATUS);
    List<String> actionStatus = List.of("1210", DOCUMENT_APPROVED_STATUS);

    List<String> rejectionsStatus = List.of("1211", "1212", "1213", "0911", "0912", "0811", "0813", "0811", "0812", "1014");

    /**
     * Generates a Smile ID smart link for user verification.
     *
     * @param smileRequest The request containing ID type and number for verification.
     * @param requirementId The ID of the requirement being verified.
     * @return A {@link SmileIdTokenResponse} containing the generated smart link and job ID.
     * @throws BadRequestException If the ID card already exists or the requirement is already completed.
     * @throws UpstreamServiceException If the token generation fails.
     */
    @Transactional
    public SmileIdTokenResponse getSmileLink(SmileIdIdTypeRequest smileRequest, Long requirementId) {

        // Check if a user has already completed this requirement
        if (userOnboardingRepository.existsByUserIdAndRequirementIdAndCompleted(AppUtil.getLoggedInUserId(),
                requirementId, true)) {
            throw new BadRequestException("User has already completed this requirement");
        }

        Requirements requirements = requirementsRepository.findByIdAndStatus(requirementId, EntityStatus.ACTIVE.getValue())
                .orElseThrow(() -> new BadRequestException("Requirement not found"));

        try {

            String userId = AppUtil.getLoggedInUserEmail();
            String timestamp = new SimpleDateFormat(DATE_TIME_FORMAT).format(System.currentTimeMillis());
            String jobId = UUID.randomUUID().toString();
            String signature = generateSignature(timestamp);
            String expiresAt = LocalDate.now().plusDays(smileIdProperties.expiresAt()).atStartOfDay(ZoneId.systemDefault()).toInstant().toString();

            SmileIdSmileLinkRequest request = SmileIdSmileLinkRequest.builder()
                    .partnerId(smileIdProperties.partnerId()).signature(signature)
                    .name(AppUtil.getLoggedInUserFullName())
                    .timestamp(timestamp)
                    .companyName(oneAppUsersProperties.companyName()).dataPrivacyPolicyUrl(oneAppUsersProperties.dataPrivacyPolicyUrl())
                    .logoUrl(oneAppUsersProperties.logoUrl()).isSingleUse(smileIdProperties.isSingleUse())
                    .expiresAt(expiresAt).userId(userId)
                    .idTypes(smileRequest.smileRequest()).partnerParams(Map.of("job_id", jobId)).build();

            smileIdRecordRepository.save(SmileIdRecord.builder().jobId(jobId).requirementId(requirements.getId()).userId(userId)
                    .timestamp(timestamp).build());

            if (callSmileId) {
                return getSmartLinkResponse(request, jobId);
            } else {
                return getTestSmartLinkResponse(jobId, signature, timestamp);
            }

        } catch (Exception e) {
            throw new UpstreamServiceException("Can not generate token.");
        }
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

        try {
            SmileIdRecord record = smileIdRecordRepository.findSmileIdRecordByJobId(request.partnerParams().jobId());
            if (!confirmSignature(request.signature(), request.timestamp())) {
                return new SmileIdWebhookResponse("Failed", false);
            }
            if (rejectionsStatus.contains(request.resultCode())) {
                handleFailedNotification(request, record);
            } else if (actionStatus.contains(request.resultCode()) || dataStatus.contains(request.resultCode())) {
                handleSuccessfulNotification(request, record);
                response = new SmileIdWebhookResponse("Success", true);
                messagingTemplate.convertAndSend("/topic/smile-id/" + request.partnerParams().jobId(), response);
                return response;
            }
        } catch (RuntimeException e) {
            messagingTemplate.convertAndSend("/topic/smile-id/" + request.partnerParams().jobId(), response);
            log.error(e.getMessage(), e);
            throw new BadRequestException("Bad request: invalid request");
        }
        messagingTemplate.convertAndSend("/topic/smile-id/" + request.partnerParams().jobId(), response);

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
        UserDocument document = UserDocument.builder().userId(loggedInUser.getId()).requirementId(smileIdRecord.getRequirementId())
                .idType(notification.idType()).additionalUrl(notification.kycReceipt()).build();
        if (nonNull(notification.imageLinks())) {
            document.setIdCardFront(notification.imageLinks().idCardImage());
            document.setIdCardBack(notification.imageLinks().idCardBack());
            document.setSelfieImage(notification.imageLinks().selfieImage());
        }
        userDocumentRepository.save(document);

        if (AppUtil.nonIsNull(notification.idNumber(), notification.idType())) {
            idCardRepository.save(IdCard.builder().idValue(notification.idNumber())
                    .idCardType(notification.idType())
                    .expiryDate(notification.expirationDate())
                    .issuedDate(notification.issuanceDate())
                    .userId(loggedInUser.getId())
                    .build());
        }

        if (OnboardingRequirements.of(notification.idType()) == OnboardingRequirements.BVN) {

            UserProfile profile = userProfileRepository.findByUserId(loggedInUser.getId()).orElseThrow(() -> new BadRequestException("User not found"));

            profile.setGender(Gender.getGender(notification.gender()).getCaps());
            profile.setDateOfBirth(LocalDate.parse(notification.dob()));
            profile.setCountryOfOrigin(getCountry(notification));
            profile.setLgOfOrigin(getLgo(notification));
            profile.setStateOfOrigin(notification.placeOfBirth());
            userProfileRepository.save(profile);

            requireNonNull(cacheManager.getCache(AppConstants.USERS_CACHE_NAME)).evict(loggedInUser.getEmail());
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
     * Generates a signature for Smile ID API requests using HMAC-SHA256.
     *
     * @param timestamp The timestamp to include in the signature.
     * @return The generated signature as a Base64-encoded string.
     * @throws UpstreamServiceException If the signature generation fails.
     */
    private String generateSignature(String timestamp) {
        try {
            Mac mac = getMac(timestamp);
            return Base64.getEncoder().encodeToString(mac.doFinal());
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new UpstreamServiceException("Can not generate token.");
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

    /**
     * Sends a request to the Smile ID API to generate a smart link.
     *
     * @param request The request containing the details for the smart link.
     * @param jobId The job ID associated with the request.
     * @return A {@link SmileIdTokenResponse} containing the generated smart link and job ID.
     */
    private SmileIdTokenResponse getSmartLinkResponse(SmileIdSmileLinkRequest request, String jobId) {
        SmileIdSmileLinkResponse response = smileIdClient.createSmileLink(request);
        return new SmileIdTokenResponse(response.link(), jobId);
    }

    /**
     * Generates a test smart link response for local or development environments.
     *
     * @param jobId The job ID associated with the test response.
     * @param signature A placeholder value for the test response.
     * @param timestamp A placeholder value for the test response.
     * @return A {@link SmileIdTokenResponse} containing the test smart link and job ID.
     */
    private SmileIdTokenResponse getTestSmartLinkResponse(String jobId, String signature, String timestamp) {
        return new SmileIdTokenResponse("", jobId, signature, timestamp);
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
