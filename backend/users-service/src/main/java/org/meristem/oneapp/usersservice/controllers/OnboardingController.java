package org.meristem.oneapp.usersservice.controllers;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.meristem.oneapp.usersservice.constants.ApiConstants;
import org.meristem.oneapp.usersservice.domains.requests.AddressOnboardRequest;
import org.meristem.oneapp.usersservice.domains.requests.ProcessAddressRequest;
import org.meristem.oneapp.usersservice.domains.requests.SmileIdIdTypeRequest;
import org.meristem.oneapp.usersservice.domains.responses.SmileIdWebhookNotification;
import org.meristem.oneapp.usersservice.domains.responses.*;
import org.meristem.oneapp.usersservice.services.OnboardingService;
import org.meristem.oneapp.usersservice.services.SmileIdService;
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
    private final SmileIdService smileIdService;

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

    @Operation(summary = "Get smile id token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Allows the users to get smile id token for smile id verifications")
    })
    @PreAuthorize("hasRole('ROLE_users.get_smile_id_token')")
    @PostMapping(value = "/smile-id/smart-link", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<SmileIdTokenResponse>> getSmileIdToken(@RequestBody @Valid SmileIdIdTypeRequest idType, @RequestParam(name = "requirement-id") Long requirementId) {
        return ApiUtil.buildResponse(smileIdService.getSmileLink(idType, requirementId), HttpStatus.OK.toString(), "Token successfully generated");
    }

    @Operation(summary = "Smile Id webhook")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Allows Smile Id to send webhook notifications to us")
    })
    @PostMapping(value = "/smile-id/webhook", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<SmileIdWebhookResponse>> smileIdWebhook(@RequestBody @NotNull(message = "Cannot be null") SmileIdWebhookNotification request) {
        return ApiUtil.buildResponse(smileIdService.handleWebhook(request), HttpStatus.OK.toString(), "Request successful");
    }
}
