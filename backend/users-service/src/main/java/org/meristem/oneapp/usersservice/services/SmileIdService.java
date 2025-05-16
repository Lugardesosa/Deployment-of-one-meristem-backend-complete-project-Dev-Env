package org.meristem.oneapp.usersservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.meristem.oneapp.usersservice.config.configProperties.SmileIdProperties;
import org.meristem.oneapp.usersservice.constants.AppConstants;
import org.meristem.oneapp.usersservice.domains.enums.*;
import org.meristem.oneapp.usersservice.domains.responses.SmileIdWebhookNotification;
import org.meristem.oneapp.usersservice.domains.responses.SmileIdTokenResponse;
import org.meristem.oneapp.usersservice.domains.responses.SmileIdWebhookResponse;
import org.meristem.oneapp.usersservice.exceptionHandler.exceptions.BadRequestException;
import org.meristem.oneapp.usersservice.exceptionHandler.exceptions.UpstreamServiceException;
import org.meristem.oneapp.usersservice.models.*;
import org.meristem.oneapp.usersservice.repositories.*;
import org.meristem.oneapp.usersservice.utils.AppUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smile.identity.core.Signature;
import smile.identity.core.WebApi;
import smile.identity.core.enums.Product;
import smile.identity.core.keys.SignatureKey;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static java.util.Objects.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class SmileIdService {

    private final SmileIdRecordRepository smileIdRecordRepository;
    private final UserOnboardingRepository userOnboardingRepository;
    private final RequirementsRepository requirementsRepository;
    private final UserDocumentRepository userDocumentRepository;
    private final IdCardRepository idCardRepository;
    private final UserProfileRepository userProfileRepository;
    private final UsersRepository usersRepository;
    private final CacheManager cacheManager;
    private final SimpMessagingTemplate messagingTemplate;

    @Value("${spring.profiles.active}")
    private String activeProfile;

    private final SmileIdProperties smileIdProperties;

    List<String> dataStatus = List.of("1012");
    List<String> actionStatus = List.of("1210", "0810");

    List<String> rejectionsStatus = List.of("1211", "1212", "1213", "0911", "0912", "0811", "0813", "0811", "0812", "1014");

    @Transactional
    public SmileIdTokenResponse getToken(Product product, Long requirementId) {

        try {
            String userId = AppUtil.getLoggedInUserEmail();

            Requirements requirements = requirementsRepository.findByIdAndStatus(requirementId, EntityStatus.ACTIVE.getValue())
                    .orElseThrow(() -> new BadRequestException("Requirement not found"));

            // Check if user has already completed this requirement
            if (userOnboardingRepository.existsByUserIdAndRequirementIdAndCompleted(AppUtil.getLoggedInUserId(),
                    requirementId, true)) {
                throw new BadRequestException("User has already completed this requirement");
            }


            String jobId = UUID.randomUUID().toString();

            Integer jobType = product.compareTo(Product.DOC_VERIFICATION) == 0 ? 6 : 1;

            String signature = generateSignature(jobId, jobType, userId);

            String partnerId = smileIdProperties.partnerId();
            String defaultCallback = smileIdProperties.callbackUrl();
            String apiKey = smileIdProperties.apiKey();
            // Use '0' for the sandbox server, use '1' for the production server
            String sidServer = List.of("dev", "local").contains(activeProfile) ? "0" : "1";

            WebApi connection = new WebApi(partnerId, apiKey, defaultCallback, sidServer);

            String timestamp = Instant.now().toString();

            smileIdRecordRepository.save(SmileIdRecord.builder().jobId(jobId).requirementId(requirementId).userId(userId)
                    .timestamp(timestamp).jobType(jobType).build());
            return new SmileIdTokenResponse(connection.getWebToken(timestamp, userId, jobId, product), jobId, signature);
        } catch (Exception e) {
            throw new UpstreamServiceException("Can not generate token. Error: " + e.getMessage());
        }
    }

    @Transactional
    public SmileIdWebhookResponse handleWebhook(SmileIdWebhookNotification request) {

        try {
            SmileIdRecord record = smileIdRecordRepository.findSmileIdRecordByJobId(request.partnerParams().jobId());
            if (!confirmSignature(record)) {
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

    private String generateSignature(String jobId, Integer jobType, String userId) {

        Signature signature = new Signature(smileIdProperties.partnerId(), smileIdProperties.apiKey());
        String isoTimestamp = Instant.now().toString();
        SignatureKey key = signature.getSignatureKey(isoTimestamp);
        smileIdRecordRepository.save(SmileIdRecord.builder().jobId(jobId).userId(userId).timestamp(key.getTimestamp()).jobType(jobType).build());
        return key.getSignature();
    }

    private boolean confirmSignature(SmileIdRecord smileIdRecord) {
        Signature signature = new Signature(smileIdProperties.partnerId(), smileIdProperties.apiKey());
        return signature.confirmSignature(smileIdRecord.getTimestamp(), "receivedSignatureString");
    }
}
