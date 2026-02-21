package org.meristem.oneapp.usersservice.services.implementations;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.obs.services.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.meristem.oneapp.usersservice.constants.KafkaTopics;
import org.meristem.oneapp.usersservice.domains.enums.*;
import org.meristem.oneapp.usersservice.domains.responses.SmileIdWebhookNotification;
import org.meristem.oneapp.kafka.dtos.UploadImageDto;
import org.meristem.oneapp.usersservice.dtos.IdQueryDetailsDto;
import org.meristem.oneapp.usersservice.mappers.UserIdDetailsMapper;
import org.meristem.oneapp.usersservice.models.Files;
import org.meristem.oneapp.usersservice.models.OutboxEvent;
import org.meristem.oneapp.usersservice.models.UserIdDetails;
import org.meristem.oneapp.usersservice.models.Users;
import org.meristem.oneapp.usersservice.repositories.CustomRepository;
import org.meristem.oneapp.usersservice.repositories.FilesRepository;
import org.meristem.oneapp.usersservice.repositories.OutboxEventRepository;
import org.meristem.oneapp.usersservice.services.IIdDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class IdDetailsService implements IIdDetailsService {

    private final UserIdDetailsMapper userIdDetailsMapper = UserIdDetailsMapper.INSTANCE;
    private final HuaweiService huaweiService;
    private final FilesRepository filesRepository;
    private final CustomRepository customRepository;
    private final ObjectMapper objectMapper;
    private final OutboxEventRepository outboxEventRepository;

    public UserIdDetails buildAndSaveIdDetails(IdQueryDetailsDto notification, Users loggedInUser) {

        Optional<UserIdDetails> userIdDetailsOpt = customRepository.findOneBy(UserIdDetails.class, Map.of("userId", loggedInUser.getId(), "idType", IdCardType.fromName(notification.getIdType()).getName()));

        if (userIdDetailsOpt.isPresent()) {
            return userIdDetailsOpt.get();
        }
        UserIdDetails userIdDetails = userIdDetailsMapper.smileIdWebhookNotificationToUserIdDetails(notification);
        userIdDetails.setIdType(IdCardType.fromName(notification.getIdType()).getName());
        userIdDetails.setGender(Gender.getGender(notification.getGender()).getCaps());
        userIdDetails.setUserId(loggedInUser.getId());

        if (IdCardType.BVN.getName().equalsIgnoreCase(userIdDetails.getIdType())) {
            userIdDetails.setNote("BVN verified successfully");
            userIdDetails.setValidated(true);
        }
        customRepository.save(userIdDetails);

        if (StringUtils.isNotBlank(notification.getPhoto())) {
            try {
                UploadImageDto built = UploadImageDto.builder().photo(notification.getPhoto())
                        .userId(loggedInUser.getId())
                        .userIdDetailsId(userIdDetails.getId()).build();
                OutboxEvent image = OutboxEvent.builder()
                        .aggregateId(userIdDetails.getId()).aggregateType(AggregateType.USER.getValue())
                        .eventType(KafkaTopics.KAFKA_KYC_IMAGE_UPLOAD_TOPIC)
                        .outboxStatus(OutboxStatus.PENDING.getValue())
                        .eventClass(UploadImageDto.class.getName())
                        .eventKey(userIdDetails.getId().toString())
                        .payload(objectMapper.writeValueAsString(built))
                        .build();
                outboxEventRepository.save(image);
            } catch (JsonProcessingException e) {
                log.error("Error uploading image for user with id {} to outbox", loggedInUser.getId(), e);
            }
        }
        return userIdDetails;
    }


    @Transactional
    public void uploadImage(UploadImageDto dto) {
        String base64ImageString = dto.photo();
        if (base64ImageString.startsWith("data:")) {
            base64ImageString = base64ImageString.split(",")[1];
        }
        byte[] imageBytes = Base64.getDecoder().decode(base64ImageString);

        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes)) {
            String objectKey = HuaweiService.sanitiseEmail(dto.userEmail()).concat(String.valueOf(System.currentTimeMillis())).concat("-").concat(UUID.randomUUID().toString());
            String contentType = HuaweiService.detectContentType(inputStream);
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(contentType);
            boolean uploaded = huaweiService.uploadFile(inputStream, FileType.IMAGE, objectKey, objectMetadata);

            if (uploaded) {
                customRepository.findById(UserIdDetails.class, dto.userIdDetailsId()).ifPresent(
                        uid -> {
                            Files files = filesRepository.save(Files.builder().userId(dto.userId()).fileKey(objectKey).contentType(contentType).fileType(FileType.IMAGE.getValue()).build());
                            uid.setFileId(files.getId());
                            customRepository.save(uid);
                            log.info("Image uploaded and saved for user {}", dto.userId());
                        }
                );
            }
        } catch (IOException ignore) {
            log.error("Could not upload users id image for user with id {}", dto.userId());
        }
    }
}
