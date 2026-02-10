package org.meristem.oneapp.usersservice.services.implementations;

import com.obs.services.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.meristem.oneapp.usersservice.domains.enums.FileType;
import org.meristem.oneapp.usersservice.domains.enums.Gender;
import org.meristem.oneapp.usersservice.domains.enums.IdCardType;
import org.meristem.oneapp.usersservice.domains.responses.SmileIdWebhookNotification;
import org.meristem.oneapp.usersservice.mappers.UserIdDetailsMapper;
import org.meristem.oneapp.usersservice.models.Files;
import org.meristem.oneapp.usersservice.models.UserIdDetails;
import org.meristem.oneapp.usersservice.models.Users;
import org.meristem.oneapp.usersservice.repositories.CustomRepository;
import org.meristem.oneapp.usersservice.repositories.FilesRepository;
import org.meristem.oneapp.usersservice.services.IIdDetailsService;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class IdDetailsService implements IIdDetailsService {

    private final CustomRepository customRepository;
    private final UserIdDetailsMapper userIdDetailsMapper = UserIdDetailsMapper.INSTANCE;
    private final HuaweiService huaweiService;
    private final FilesRepository filesRepository;

    public void saveIdDetails(SmileIdWebhookNotification notification, Users loggedInUser) {

        customRepository.findOneBy(UserIdDetails.class, Map.of("userId", loggedInUser.getId(), "idType", IdCardType.fromName(notification.getIdType()).getName()))
                .ifPresentOrElse(u -> {
                }, () -> {
                    UserIdDetails userIdDetails = userIdDetailsMapper.smileIdWebhookNotificationToUserIdDetails(notification);
                    userIdDetails.setIdType(IdCardType.fromName(notification.getIdType()).getName());
                    userIdDetails.setGender(Gender.getGender(notification.getGender()).getCaps());
                    userIdDetails.setUserId(loggedInUser.getId());

                    if (StringUtils.isNotBlank(notification.getPhoto())) {
                        String base64ImageString = notification.getPhoto();
                        if (base64ImageString.startsWith("data:")) {
                            base64ImageString = base64ImageString.split(",")[1];
                        }
                        byte[] imageBytes = Base64.getDecoder().decode(base64ImageString);

                        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes)) {
                            String objectKey = HuaweiService.sanitiseEmail(loggedInUser.getEmail()).concat(String.valueOf(System.currentTimeMillis())).concat("-").concat(UUID.randomUUID().toString());
                            String contentType = HuaweiService.detectContentType(inputStream);
                            ObjectMetadata objectMetadata = new ObjectMetadata();
                            objectMetadata.setContentType(contentType);
                            boolean uploaded = huaweiService.uploadFile(inputStream, FileType.IMAGE, objectKey,  objectMetadata);

                            if (uploaded) {
                                Files files = filesRepository.save(Files.builder().userId(loggedInUser.getId()).fileKey(objectKey).contentType(contentType).fileType(FileType.IMAGE.getValue()).build());
                                userIdDetails.setFileId(files.getId());
                            }
                        } catch (IOException ignore) {
                        }
                    }
                    customRepository.save(userIdDetails);
                });
    }
}
