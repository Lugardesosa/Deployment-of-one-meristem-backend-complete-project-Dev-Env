package org.meristem.oneapp.notificationservice.services;

import jakarta.validation.Valid;
import org.meristem.oneapp.notificationservice.domains.requests.OneSignalUserDeviceUpdateRequest;
import org.meristem.oneapp.notificationservice.domains.requests.UserDeviceRegistrationRequest;
import org.meristem.oneapp.notificationservice.domains.requests.UserDeviceUpdateRequest;
import org.meristem.oneapp.notificationservice.domains.responses.UserDeviceRegistrationResponse;

public interface IUserDeviceRegistrationService {
    UserDeviceRegistrationResponse registerUserDevice(UserDeviceRegistrationRequest request);
    UserDeviceRegistrationResponse updateUserDeviceExpo(@Valid UserDeviceUpdateRequest request);
    UserDeviceRegistrationResponse registerUserDeviceOneSignal(UserDeviceRegistrationRequest request);
    UserDeviceRegistrationResponse updateUserDeviceOneSignal(@Valid OneSignalUserDeviceUpdateRequest request);
}
