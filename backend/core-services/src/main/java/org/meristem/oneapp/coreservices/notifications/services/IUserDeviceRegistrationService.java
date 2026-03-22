package org.meristem.oneapp.coreservices.notifications.services;

import jakarta.validation.Valid;
import org.meristem.oneapp.coreservices.notifications.domains.requests.OneSignalUserDeviceUpdateRequest;
import org.meristem.oneapp.coreservices.notifications.domains.requests.UserDeviceRegistrationRequest;
import org.meristem.oneapp.coreservices.notifications.domains.requests.UserDeviceUpdateRequest;
import org.meristem.oneapp.coreservices.notifications.domains.responses.UserDeviceRegistrationResponse;

public interface IUserDeviceRegistrationService {
    UserDeviceRegistrationResponse registerUserDevice(UserDeviceRegistrationRequest request);
    UserDeviceRegistrationResponse updateUserDeviceExpo(@Valid UserDeviceUpdateRequest request);
    UserDeviceRegistrationResponse updateUserDeviceOneSignal(@Valid OneSignalUserDeviceUpdateRequest request);
}
