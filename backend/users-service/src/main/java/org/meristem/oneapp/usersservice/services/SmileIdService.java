package org.meristem.oneapp.usersservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.meristem.oneapp.usersservice.config.configProperties.OneAppProperties;
import org.meristem.oneapp.usersservice.config.configProperties.SmileIdProperties;
import org.meristem.oneapp.usersservice.constants.AppConstants;
import org.meristem.oneapp.usersservice.domains.enums.*;
import org.meristem.oneapp.usersservice.domains.requests.SmileIdIdTypeRequest;
import org.meristem.oneapp.usersservice.domains.responses.SmileIdWebhookNotification;
import org.meristem.oneapp.usersservice.domains.responses.SmileIdTokenResponse;
import org.meristem.oneapp.usersservice.domains.responses.SmileIdWebhookResponse;
import org.meristem.oneapp.usersservice.exceptionHandler.exceptions.BadRequestException;
import org.meristem.oneapp.usersservice.exceptionHandler.exceptions.UpstreamServiceException;
import org.meristem.oneapp.usersservice.integrations.SmileIdClient;
import org.meristem.oneapp.usersservice.integrations.requests.SmileIdSmileLinkRequest;
import org.meristem.oneapp.usersservice.integrations.responses.SmileIdSmileLinkResponse;
import org.meristem.oneapp.usersservice.models.*;
import org.meristem.oneapp.usersservice.repositories.*;
import org.meristem.oneapp.usersservice.utils.AppUtil;
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

@Slf4j
@RequiredArgsConstructor
@Service
public class SmileIdService {

    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
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


    private final SmileIdProperties smileIdProperties;
    private final OneAppProperties oneAppProperties;

    List<String> dataStatus = List.of("1012");
    List<String> actionStatus = List.of("1210", "0810");

    List<String> rejectionsStatus = List.of("1211", "1212", "1213", "0911", "0912", "0811", "0813", "0811", "0812", "1014");

    @Transactional
    public SmileIdTokenResponse getSmileLink(SmileIdIdTypeRequest smileRequest, Long requirementId) {

        try {
            // Check if user has already completed this requirement
            if (userOnboardingRepository.existsByUserIdAndRequirementIdAndCompleted(AppUtil.getLoggedInUserId(),
                    requirementId, true)) {
                throw new BadRequestException("User has already completed this requirement");
            }

            String userId = AppUtil.getLoggedInUserEmail();
            String timestamp = new SimpleDateFormat(DATE_TIME_FORMAT).format(System.currentTimeMillis());
            String jobId = UUID.randomUUID().toString();

            SmileIdSmileLinkRequest request = SmileIdSmileLinkRequest.builder()
                    .partnerId(smileIdProperties.partnerId()).signature(generateSignature(timestamp))
                    .timestamp(timestamp).callbackUrl(smileIdProperties.callbackUrl())
                    .companyName(oneAppProperties.companyName()).dataPrivacyPolicyUrl(oneAppProperties.dataPrivacyPolicyUrl())
                    .logoUrl(oneAppProperties.logoUrl()).isSingleUse(smileIdProperties.isSingleUse())
                    .expiresAt(LocalDate.now().plusDays(smileIdProperties.expiresAt()).atStartOfDay(ZoneId.systemDefault()).toInstant().toString())
                    .idTypes(smileRequest.idTypes()).partnerParams(Map.of("job_id", jobId)).build();

            Requirements requirements = requirementsRepository.findByIdAndStatus(requirementId, EntityStatus.ACTIVE.getValue())
                    .orElseThrow(() -> new BadRequestException("Requirement not found"));

            smileIdRecordRepository.save(SmileIdRecord.builder().jobId(jobId).requirementId(requirements.getId()).userId(userId)
                    .timestamp(timestamp).build());

            SmileIdSmileLinkResponse response = smileIdClient.createSmileLink(request);

            log.info("Token: {}", response.link());
            return new SmileIdTokenResponse(response.link(), response.refId());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new UpstreamServiceException("Can not generate token.");
        }
    }

    @Transactional
    public SmileIdWebhookResponse handleWebhook(SmileIdWebhookNotification request) {

        try {
            SmileIdRecord record = smileIdRecordRepository.findSmileIdRecordByJobId(request.partnerParams().jobId());
            if (!confirmSignature(request.signature(), request.timestamp())) {
                return new SmileIdWebhookResponse("Failed", false);
            }
            if (rejectionsStatus.contains(request.resultCode())) {
                handleFailedNotification(request, record, record.getUserId());
            } else if (actionStatus.contains(request.resultCode()) || dataStatus.contains(request.resultCode())) {
                handleSuccessfulNotification(request, record, record.getUserId());
            }
        } catch (RuntimeException e) {
            return new SmileIdWebhookResponse("Failed", false);
        }
        SmileIdWebhookResponse response = new SmileIdWebhookResponse("Success", true);
        messagingTemplate.convertAndSend("/topic/smile-id/" + request.partnerParams().jobId(), response);

        return response;
    }

    private void handleFailedNotification(SmileIdWebhookNotification notification, SmileIdRecord smileIdRecord, String userId) {
        smileIdRecord.setMessage(notification.resultText());
        smileIdRecord.setStatus(SmileIdRecordStatus.FAILED.getValue());
        userOnboardingRepository.markOnboardingAsFailed(userId, smileIdRecord.getRequirementId(), OnboardingStatus.REJECTED.getValue());
        smileIdRecordRepository.save(smileIdRecord);
    }

    private void handleSuccessfulNotification(SmileIdWebhookNotification notification, SmileIdRecord smileIdRecord, String userId) {
        if (actionStatus.contains(notification.resultCode())) {
            handleAction(notification, smileIdRecord, userId);
        } else {
            handleData(notification, smileIdRecord, userId);
        }
    }

    private void handleAction(SmileIdWebhookNotification notification, SmileIdRecord smileIdRecord, String userId) {
        smileIdRecord.setMessage(notification.resultText());
        smileIdRecord.setStatus(SmileIdRecordStatus.APPROVED.getValue());
        userOnboardingRepository.markOnboardingAsFailed(userId, smileIdRecord.getRequirementId(), OnboardingStatus.APPROVED.getValue());
        smileIdRecordRepository.save(smileIdRecord);
    }

    private void handleData(SmileIdWebhookNotification notification, SmileIdRecord smileIdRecord, String userId) {
        Requirements requirements = requirementsRepository.findByIdAndStatus(smileIdRecord.getRequirementId(), EntityStatus.ACTIVE.getValue())
                .orElseThrow(() -> new BadRequestException("Requirement not found"));

        // Save document url for non bvn requirement
        Users loggedInUser = usersRepository.findOneByEmail(userId);
        UserDocument document = UserDocument.builder().userId(loggedInUser.getId()).requirementId(smileIdRecord.getRequirementId())
                .idType(notification.idType()).additionalUrl(notification.kycReceipt()).build();
        if (nonNull(notification.imageLinks())) {
            document.setIdCardFront(notification.imageLinks().idCardImage());
            document.setIdCardBack(notification.imageLinks().idCardBack());
            document.setSelfieImage(notification.imageLinks().selfieImage());
        }
        userDocumentRepository.save(document);

        idCardRepository.save(IdCard.builder().idValue(notification.idNumber())
                .idCardType(notification.idType())
                .expiryDate(notification.expirationDate())
                .issuedDate(notification.issuanceDate())
                .userId(loggedInUser.getId())
                .build());

        if (OnboardingRequirements.of(notification.idType()) == OnboardingRequirements.BVN) {
            userProfileRepository.updateUsersDobAndGender(notification.gender(), LocalDate.parse(notification.dob()), loggedInUser.getId());
            requireNonNull(cacheManager.getCache(AppConstants.USERS_CACHE_NAME)).evict(loggedInUser.getEmail());
        }

        // Check if all requirement has been completed, mark the user as completed onboarding
        if (userOnboardingRepository.allRequirementsSubmitted(loggedInUser.getId())) {
            userProfileRepository.completeOnboarding(loggedInUser.getId());
        }

    }

    private String generateSignature(String timestamp) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(smileIdProperties.apiKey().getBytes(), "HmacSHA256"));
            mac.update(timestamp.getBytes(StandardCharsets.UTF_8));
            mac.update(smileIdProperties.partnerId().getBytes(StandardCharsets.UTF_8));
            mac.update("sid_request".getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(mac.doFinal());
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new UpstreamServiceException("Can not generate token.");
        }
    }

    private boolean confirmSignature(String receivedSignature, String receivedTimestamp) {

        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(smileIdProperties.apiKey().getBytes(), "HmacSHA256"));
            mac.update(receivedTimestamp.getBytes(StandardCharsets.UTF_8));
            mac.update(smileIdProperties.partnerId().getBytes(StandardCharsets.UTF_8));
            mac.update("sid_request".getBytes(StandardCharsets.UTF_8));

            String generatedSignature = Base64.getEncoder().encodeToString(mac.doFinal());

            return generatedSignature.equals(receivedSignature);
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }
}
