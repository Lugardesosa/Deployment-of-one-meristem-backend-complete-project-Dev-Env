package org.meristem.oneapp.notificationservice.services.implementations;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.meristem.oneapp.notificationservice.domains.requests.OneSignalUserDeviceUpdateRequest;
import org.meristem.oneapp.notificationservice.domains.requests.UserDeviceRegistrationRequest;
import org.meristem.oneapp.notificationservice.domains.requests.UserDeviceUpdateRequest;
import org.meristem.oneapp.notificationservice.domains.responses.UserDeviceRegistrationResponse;
import org.meristem.oneapp.notificationservice.exception.exceptions.BadRequestException;
import org.meristem.oneapp.notificationservice.models.OneSignalSubscriptions;
import org.meristem.oneapp.notificationservice.models.UserExpoTokens;
import org.meristem.oneapp.notificationservice.repositories.CustomRepository;
import org.meristem.oneapp.notificationservice.repositories.OneSignalSubscriptionsRepository;
import org.meristem.oneapp.notificationservice.repositories.UserExpoTokensRepository;
import org.meristem.oneapp.notificationservice.services.IPushNotificationService;
import org.meristem.oneapp.notificationservice.services.IUserDeviceRegistrationService;
import org.meristem.oneapp.notificationservice.utils.AppUtil;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserDeviceRegistrationService implements IUserDeviceRegistrationService {

    private final IPushNotificationService pushNotificationService;
    private final CustomRepository customRepository;
    private final UserExpoTokensRepository userExpoTokensRepository;
    ;
    private final OneSignalSubscriptionsRepository oneSignalSubscriptionsRepository;

    public UserDeviceRegistrationResponse registerUserDevice(UserDeviceRegistrationRequest request) {

        if (userExpoTokensRepository.existsByExpoToken(request.expoToken())) {
            throw new BadRequestException("Token already registered");
        }
        UserExpoTokens userExpoTokens = UserExpoTokens.builder().expoToken(request.expoToken())
                .deviceId(request.deviceId()).build();
        Long authUserId = AppUtil.getAuthUserId();
        if (authUserId != null) {
            userExpoTokens.setUserId(authUserId);
        }
        customRepository.save(userExpoTokens);
        return new UserDeviceRegistrationResponse("Created", true);
    }

    public UserDeviceRegistrationResponse registerUserDeviceOneSignal(UserDeviceRegistrationRequest request) {

        if (userExpoTokensRepository.existsByExpoToken(request.expoToken())) {
            throw new BadRequestException("Token already registered");
        }
        OneSignalSubscriptions userExpoTokens = OneSignalSubscriptions.builder().subscriptionId(request.expoToken())
                .deviceId(request.deviceId()).build();
        Long authUserId = AppUtil.getAuthUserId();
        if (authUserId != null) {
            userExpoTokens.setUserId(authUserId);
        }
        customRepository.save(userExpoTokens);
        return new UserDeviceRegistrationResponse("Created", true);
    }

    public UserDeviceRegistrationResponse updateUserDeviceExpo(@Valid UserDeviceUpdateRequest request) {

        UserExpoTokens userExpoTokens = userExpoTokensRepository.findOneByDeviceId(request.deviceId()).orElseThrow(() -> new BadRequestException("Device not found"));
        userExpoTokens.setUserId(AppUtil.getLoggedInUserId());
        userExpoTokensRepository.save(userExpoTokens);
        return new UserDeviceRegistrationResponse("Updated", true);
    }

    @Override
    public UserDeviceRegistrationResponse updateUserDeviceOneSignal(OneSignalUserDeviceUpdateRequest request) {
        OneSignalSubscriptions oneSignalSubscriptions = oneSignalSubscriptionsRepository.findOneByDeviceId(request.deviceId()).orElseThrow(() -> new BadRequestException("Device not found"));
        oneSignalSubscriptions.setUserId(AppUtil.getLoggedInUserId());
        oneSignalSubscriptionsRepository.save(oneSignalSubscriptions);
        return new UserDeviceRegistrationResponse("Updated", true);
    }
}
