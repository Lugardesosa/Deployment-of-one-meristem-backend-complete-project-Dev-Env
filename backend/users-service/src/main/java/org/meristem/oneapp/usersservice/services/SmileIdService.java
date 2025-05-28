package org.meristem.oneapp.usersservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.meristem.oneapp.kafka.dtos.KycCompletedDto;
import org.meristem.oneapp.usersservice.config.configProperties.OneAppUsersProperties;
import org.meristem.oneapp.usersservice.config.configProperties.SmileIdProperties;
import org.meristem.oneapp.usersservice.constants.AppConstants;
import org.meristem.oneapp.usersservice.constants.KafkaTopics;
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
import org.meristem.oneapp.usersservice.models.*;
import org.meristem.oneapp.usersservice.repositories.*;
import org.meristem.oneapp.usersservice.utils.AppUtil;
import org.springframework.cache.CacheManager;
import org.springframework.kafka.support.KafkaHeaders;
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

@Slf4j
@RequiredArgsConstructor
@Service
public class SmileIdService {

    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
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
    private final SimpMessagingTemplate messagingTemplate;
    private final SmileIdClient smileIdClient;
    private final KafkaSenderService kafkaSenderService;


    private final SmileIdProperties smileIdProperties;
    private final OneAppUsersProperties oneAppUsersProperties;

    List<String> dataStatus = List.of("1012");
    List<String> actionStatus = List.of("1210", DOCUMENT_APPROVED_STATUS);

    List<String> rejectionsStatus = List.of("1211", "1212", "1213", "0911", "0912", "0811", "0813", "0811", "0812", "1014");

    @Transactional
    public SmileIdTokenResponse getSmileLink(SmileIdIdTypeRequest smileRequest, Long requirementId) {

        if (idCardRepository.existsByIdValue(smileRequest.idNumber())) {
            throw new BadRequestException("Id card already exists");
        }
        // Check if user has already completed this requirement
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
                    .timestamp(timestamp).callbackUrl(smileIdProperties.callbackUrl())
                    .companyName(oneAppUsersProperties.companyName()).dataPrivacyPolicyUrl(oneAppUsersProperties.dataPrivacyPolicyUrl())
                    .logoUrl(oneAppUsersProperties.logoUrl()).isSingleUse(smileIdProperties.isSingleUse())
                    .expiresAt(expiresAt).userId(userId)
                    .idTypes(smileRequest.smileRequest()).partnerParams(Map.of("job_id", jobId)).build();

            smileIdRecordRepository.save(SmileIdRecord.builder().jobId(jobId).requirementId(requirements.getId()).userId(userId)
                    .timestamp(timestamp).build());

            SmileIdSmileLinkResponse response = smileIdClient.createSmileLink(request);

            return new SmileIdTokenResponse(response.link(), jobId);
        } catch (Exception e) {
            throw new UpstreamServiceException("Can not generate token.");
        }
    }

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
            return new SmileIdWebhookResponse("Failed", false);
        }
        messagingTemplate.convertAndSend("/topic/smile-id/" + request.partnerParams().jobId(), response);

        return response;
    }

    private void handleFailedNotification(SmileIdWebhookNotification notification, SmileIdRecord smileIdRecord) {
        Users loggedInUser = usersRepository.findOneByEmail(smileIdRecord.getUserId());
        smileIdRecord.setMessage(notification.resultText());
        smileIdRecord.setStatus(SmileIdRecordStatus.FAILED.getValue());
        userOnboardingRepository.updateUserOnboardingStatus(loggedInUser.getId(), smileIdRecord.getRequirementId(), OnboardingStatus.REJECTED.getValue(), false);
        smileIdRecordRepository.save(smileIdRecord);
    }

    private void handleSuccessfulNotification(SmileIdWebhookNotification notification, SmileIdRecord smileIdRecord) {
        if (actionStatus.contains(notification.resultCode()) && !DOC_AND_ENHANCED_JOB_TYPES.contains(notification.partnerParams().jobType())) {
            handleAction(notification, smileIdRecord);
        } else {
            handleData(notification, smileIdRecord);
        }
    }

    private void handleAction(SmileIdWebhookNotification notification, SmileIdRecord smileIdRecord) {
        Users loggedInUser = usersRepository.findOneByEmail(smileIdRecord.getUserId());
        smileIdRecord.setMessage(notification.resultText());
        smileIdRecord.setStatus(SmileIdRecordStatus.APPROVED.getValue());
        userOnboardingRepository.updateUserOnboardingStatus(loggedInUser.getId(), smileIdRecord.getRequirementId(), OnboardingStatus.APPROVED.getValue(), true);
        // Check if all requirement has been completed, mark the user as completed onboarding
        if (userOnboardingRepository.allRequirementsSubmitted(loggedInUser.getId())) {
            userProfileRepository.completeOnboarding(loggedInUser.getId());
            kafkaSenderService.send(KycCompletedDto.builder().userId(loggedInUser.getId()).fullName(AppUtil.getUserFullName(loggedInUser)).build(), Map.of(KafkaHeaders.TOPIC, KafkaTopics.KAFKA_KYC_COMPLETED));
        }
        smileIdRecordRepository.save(smileIdRecord);
    }

    private void handleData(SmileIdWebhookNotification notification, SmileIdRecord smileIdRecord) {
        Requirements requirements = requirementsRepository.findByIdAndStatus(smileIdRecord.getRequirementId(), EntityStatus.ACTIVE.getValue())
                .orElseThrow(() -> new BadRequestException("Requirement not found"));

        // Save document url for non bvn requirement
        Users loggedInUser = usersRepository.findOneByEmail(smileIdRecord.getUserId());
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
            userProfileRepository.updateUsersDobAndGender(notification.gender(), LocalDate.parse(notification.dob()), loggedInUser.getId());
            requireNonNull(cacheManager.getCache(AppConstants.USERS_CACHE_NAME)).evict(loggedInUser.getEmail());
        }

        if (DOCUMENT_APPROVED_STATUS.equals(notification.resultCode())) {
            userOnboardingRepository.updateUserOnboardingStatus(loggedInUser.getId(), smileIdRecord.getRequirementId(), OnboardingStatus.APPROVED.getValue(), true);
            // Check if all requirement has been completed, mark the user as completed onboarding
            if (userOnboardingRepository.allRequirementsSubmitted(loggedInUser.getId())) {
                userProfileRepository.completeOnboarding(loggedInUser.getId());
                kafkaSenderService.send(KycCompletedDto.builder().userId(loggedInUser.getId()).fullName(AppUtil.getUserFullName(loggedInUser)).build(), Map.of(KafkaHeaders.TOPIC, KafkaTopics.KAFKA_KYC_COMPLETED));
            }
        }
    }

    private String generateSignature(String timestamp) {
        try {
            Mac mac = getMac(timestamp);
            return Base64.getEncoder().encodeToString(mac.doFinal());
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new UpstreamServiceException("Can not generate token.");
        }
    }

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

    private Mac getMac(String timestamp) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac mac = AppUtil.getHmacSHA256();
        mac.init(new SecretKeySpec(smileIdProperties.apiKey().getBytes(), "HmacSHA256"));
        mac.update(timestamp.getBytes(StandardCharsets.UTF_8));
        mac.update(smileIdProperties.partnerId().getBytes(StandardCharsets.UTF_8));
        mac.update("sid_request".getBytes(StandardCharsets.UTF_8));
        return mac;
    }
}
