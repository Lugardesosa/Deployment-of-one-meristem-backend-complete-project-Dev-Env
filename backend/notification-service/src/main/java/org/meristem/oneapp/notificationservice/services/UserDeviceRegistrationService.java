package org.meristem.oneapp.notificationservice.services;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.meristem.oneapp.notificationservice.domains.requests.UserDeviceRegistrationRequest;
import org.meristem.oneapp.notificationservice.domains.responses.UserDeviceRegistrationResponse;
import org.meristem.oneapp.notificationservice.models.UserExpoTokens;
import org.meristem.oneapp.notificationservice.repositories.CustomRepository;
import org.meristem.oneapp.notificationservice.utils.AppUtil;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserDeviceRegistrationService {

    private final CustomRepository customRepository;
    public UserDeviceRegistrationResponse registerUserDevice(UserDeviceRegistrationRequest request) {
        customRepository.save(UserExpoTokens.builder().expoToken(request.expoToken())
                .userId(AppUtil.getLoggedInUserId()).build());
        return new UserDeviceRegistrationResponse("Created", true);
    }
}
