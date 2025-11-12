package org.meristem.oneapp.notificationservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.meristem.oneapp.notificationservice.constants.ApiConstants;
import org.meristem.oneapp.notificationservice.domains.requests.UserDeviceRegistrationRequest;
import org.meristem.oneapp.notificationservice.domains.responses.AppResponse;
import org.meristem.oneapp.notificationservice.domains.responses.UserDeviceRegistrationResponse;
import org.meristem.oneapp.notificationservice.services.UserDeviceRegistrationService;
import org.meristem.oneapp.notificationservice.utils.ApiUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(ApiConstants.CONTEXT_PATH + "users")
public class UserSettingsController {

    private final UserDeviceRegistrationService registerUserDevice;

    @Operation(summary = "Register a user device")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Register a user device")
    })
    @PreAuthorize("hasRole('ROLE_users.device.register')")
    @PostMapping(value = "/token-register", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<UserDeviceRegistrationResponse>> updateOptionAccessed(@Valid @RequestBody UserDeviceRegistrationRequest request) {
        return ApiUtil.buildResponse(registerUserDevice.registerUserDevice(request), HttpStatus.OK.toString(), "Successful");
    }

    @PreAuthorize("hasRole('ROLE_users.device.register')")
    @PostMapping(value = "/push-token", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<String>> testPush(@Valid @RequestBody UserDeviceRegistrationRequest request) {
        return ApiUtil.buildResponse(registerUserDevice.testPush(), HttpStatus.OK.toString(), "Successful");
    }
}
