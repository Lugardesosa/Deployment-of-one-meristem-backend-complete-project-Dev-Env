package org.meristem.oneapp.notificationservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.meristem.oneapp.notificationservice.constants.ApiConstants;
import org.meristem.oneapp.notificationservice.domains.requests.UserDeviceRegistrationRequest;
import org.meristem.oneapp.notificationservice.domains.requests.UserDeviceUpdateRequest;
import org.meristem.oneapp.notificationservice.domains.responses.AppResponse;
import org.meristem.oneapp.notificationservice.domains.responses.UserDeviceRegistrationResponse;
import org.meristem.oneapp.notificationservice.services.UserDeviceRegistrationService;
import org.meristem.oneapp.notificationservice.utils.ApiUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping(ApiConstants.CONTEXT_PATH + "users")
public class UserSettingsController {

    private final UserDeviceRegistrationService registerUserDevice;

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
    @PutMapping(value = "/token-register", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<UserDeviceRegistrationResponse>> updateUserDevice(@Valid @RequestBody UserDeviceUpdateRequest request) {
        return ApiUtil.buildResponse(registerUserDevice.updateUserDevice(request), HttpStatus.OK.toString(), "Successful");
    }

    @PreAuthorize("hasRole('ROLE_users.device.register')")
    @GetMapping(value = "/push-token", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<String>> testPush(@RequestBody Map<String, String> request) {
        return ApiUtil.buildResponse(registerUserDevice.testPush(request.get("body"), request.get("title")), HttpStatus.OK.toString(), "Successful");
    }
}
