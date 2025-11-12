package org.meristem.oneapp.notificationservice.services;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.meristem.oneapp.kafka.dtos.PushNotificationDto;
import org.meristem.oneapp.notificationservice.domains.requests.UserDeviceRegistrationRequest;
import org.meristem.oneapp.notificationservice.domains.requests.UserDeviceUpdateRequest;
import org.meristem.oneapp.notificationservice.domains.responses.UserDeviceRegistrationResponse;
import org.meristem.oneapp.notificationservice.models.UserExpoTokens;
import org.meristem.oneapp.notificationservice.repositories.CustomRepository;
import org.meristem.oneapp.notificationservice.repositories.UserExpoTokensRepository;
import org.meristem.oneapp.notificationservice.utils.AppUtil;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserDeviceRegistrationService {

    private final CustomRepository customRepository;
    private final UserExpoTokensRepository userExpoTokensRepository;;
    public UserDeviceRegistrationResponse registerUserDevice(UserDeviceRegistrationRequest request) {
        UserExpoTokens userExpoTokens = UserExpoTokens.builder().expoToken(request.expoToken())
                .deviceId(request.deviceId()).build();
        Long authUserId = AppUtil.getAuthUserId();
        if (authUserId != null) {
            userExpoTokens.setUserId(authUserId);
        }
        customRepository.save(userExpoTokens);
        return new UserDeviceRegistrationResponse("Created", true);
    }

    private final PushNotificationService pushNotificationService;
    public String testPush() {
        PushNotificationDto pushNotificationDto = PushNotificationDto.builder()
                .userId(AppUtil.getLoggedInUserId())
                .body("Testing push notification")
                .title("Testing 123").build();
        pushNotificationService.sendPushNotification(pushNotificationDto);
        return "Success";
    }

    public UserDeviceRegistrationResponse updateUserDevice(@Valid UserDeviceUpdateRequest request) {

        UserExpoTokens userExpoTokens = userExpoTokensRepository.findOneByDeviceId(request.deviceId()).orElseThrow(() -> new RuntimeException("Device not found"));
        userExpoTokens.setUserId(AppUtil.getLoggedInUserId());
        userExpoTokensRepository.save(userExpoTokens);
        return new UserDeviceRegistrationResponse("Updated", true);
    }
}
