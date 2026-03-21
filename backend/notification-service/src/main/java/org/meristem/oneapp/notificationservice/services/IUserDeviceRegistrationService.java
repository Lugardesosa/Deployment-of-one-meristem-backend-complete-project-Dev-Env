package org.meristem.oneapp.notificationservice.services;

import jakarta.validation.Valid;
import org.meristem.oneapp.notificationservice.domains.requests.UserDeviceRegistrationRequest;
import org.meristem.oneapp.notificationservice.domains.requests.UserDeviceUpdateRequest;
import org.meristem.oneapp.notificationservice.domains.responses.UserDeviceRegistrationResponse;

public interface IUserDeviceRegistrationService {
    UserDeviceRegistrationResponse registerUserDevice(UserDeviceRegistrationRequest request);
    UserDeviceRegistrationResponse updateUserDevice(@Valid UserDeviceUpdateRequest request);
}
