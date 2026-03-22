package org.meristem.oneapp.coreservices.notifications.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.meristem.oneapp.coreservices.notifications.constants.ApiConstants;
import org.meristem.oneapp.coreservices.notifications.domains.requests.OneSignalUserDeviceUpdateRequest;
import org.meristem.oneapp.coreservices.notifications.domains.requests.UserDeviceRegistrationRequest;
import org.meristem.oneapp.coreservices.notifications.domains.requests.UserDeviceUpdateRequest;
import org.meristem.oneapp.coreservices.notifications.domains.responses.AppResponse;
import org.meristem.oneapp.coreservices.notifications.domains.responses.UserDeviceRegistrationResponse;
import org.meristem.oneapp.coreservices.notifications.services.IUserDeviceRegistrationService;
import org.meristem.oneapp.coreservices.notifications.utils.ApiUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(ApiConstants.CONTEXT_PATH + "users")
public class UserSettingsController {

    private final IUserDeviceRegistrationService registerUserDevice;

    @Operation(summary = "Register a user device token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Register a user device token")
    })
    @PreAuthorize("hasRole('ROLE_users.device.register') OR hasAuthority('SCOPE_device.register')")
    @PostMapping(value = "/token-register", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<UserDeviceRegistrationResponse>> registerUserDevice(@Valid @RequestBody UserDeviceRegistrationRequest request) {
        return ApiUtil.buildResponse(registerUserDevice.registerUserDevice(request), HttpStatus.OK.toString(), "Successful");
    }

    @Operation(summary = "Update a user device token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Register a user device token")
    })
    @PreAuthorize("hasRole('ROLE_users.device.register')")
    @PutMapping(value = "/token-register/expo", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<UserDeviceRegistrationResponse>> updateUserDeviceExpo(@Valid @RequestBody UserDeviceUpdateRequest request) {
        return ApiUtil.buildResponse(registerUserDevice.updateUserDeviceExpo(request), HttpStatus.OK.toString(), "Successful");
    }

    @Operation(summary = "Update a user device token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Register a user device token")
    })
    @PreAuthorize("hasRole('ROLE_users.device.register')")
    @PutMapping(value = "/token-register/one-signal", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<UserDeviceRegistrationResponse>> updateUserDevice(@Valid @RequestBody OneSignalUserDeviceUpdateRequest request) {
        return ApiUtil.buildResponse(registerUserDevice.updateUserDeviceOneSignal(request), HttpStatus.OK.toString(), "Successful");
    }
}
