package org.meristem.oneapp.usersservice.controllers;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.meristem.oneapp.usersservice.constants.ApiConstants;
import org.meristem.oneapp.usersservice.domains.requests.AddressOnboardRequest;
import org.meristem.oneapp.usersservice.domains.requests.ProcessAddressRequest;
import org.meristem.oneapp.usersservice.domains.requests.SubmitOnboardingRequest;
import org.meristem.oneapp.usersservice.domains.responses.AddressOnboardingResponse;
import org.meristem.oneapp.usersservice.domains.responses.AppResponse;
import org.meristem.oneapp.usersservice.domains.responses.SubmitOnboardingResponse;
import org.meristem.oneapp.usersservice.domains.responses.UserOnboardingResponse;
import org.meristem.oneapp.usersservice.services.OnboardingService;
import org.meristem.oneapp.usersservice.utils.ApiUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(ApiConstants.CONTEXT_PATH + "onboard")
public class OnboardingController {

    private final OnboardingService onboardingService;

    @Operation(summary = "Complete an onboarding process")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Allows users to complete an onboarding process")
    })
    @PreAuthorize("hasRole('ROLE_users.onboard.onboard')")
    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<SubmitOnboardingResponse>> onboard(@RequestBody @Valid SubmitOnboardingRequest request) {
        return ApiUtil.buildResponse(onboardingService.onboard(request), HttpStatus.CREATED.toString(), "Onboarding flow processed");
    }

    @Operation(summary = "Get the onboarding flow")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get the onboarding flow details for a user ")
    })
    @PreAuthorize("hasRole('ROLE_users.onboard.get')")
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<List<UserOnboardingResponse>>> getOnboard() {
        return ApiUtil.buildResponse(onboardingService.getOnboardingDetails(), HttpStatus.OK.toString(), "User onboarding details request successful");
    }

    @Operation(summary = "Approve or reject Address")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Allows admins to either approve or reject a user's adress ")
    })
    @PreAuthorize("hasRole('ROLE_admin.onboard.approve_address')")
    @PutMapping(value = "/address/approve", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<AddressOnboardingResponse>> approve(@RequestBody @Valid AddressOnboardRequest request) {
        return ApiUtil.buildResponse(onboardingService.approveAddress(request), HttpStatus.OK.toString(), "Address approval request successful");
    }

    @Operation(summary = "Mark an address for processing")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Allows admins to mark a user's adress for processing")
    })
    @PreAuthorize("hasRole('ROLE_admin.onboard.process_address')")
    @PutMapping(value = "/address/process", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<AddressOnboardingResponse>> process(@RequestBody @Valid ProcessAddressRequest request) {
        return ApiUtil.buildResponse(onboardingService.processAddress(request), HttpStatus.OK.toString(), "Address processing request successful");
    }
}
