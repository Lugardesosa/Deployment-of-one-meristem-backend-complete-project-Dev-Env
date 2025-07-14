package org.meristem.oneapp.usersservice.controllers;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.meristem.oneapp.usersservice.constants.ApiConstants;
import org.meristem.oneapp.usersservice.domains.requests.*;
import org.meristem.oneapp.usersservice.domains.responses.SmileIdWebhookNotification;
import org.meristem.oneapp.usersservice.domains.responses.*;
import org.meristem.oneapp.usersservice.services.OnboardingService;
import org.meristem.oneapp.usersservice.services.SmileIdService;
import org.meristem.oneapp.usersservice.utils.ApiUtil;
import org.springframework.data.domain.Page;
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
    public ResponseEntity<AppResponse<SmileIdWebhookResponse>> smileIdWebhook(@RequestBody @Valid SmileIdWebhookNotification request) {
        return ApiUtil.buildResponse(smileIdService.handleWebhook(request), HttpStatus.OK.toString(), "Request successful");
    }

    @Operation(summary = "Ok Hi webhook")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Allows OkHi to send webhook notifications to us")
    })
    @PostMapping(value = "/okhi/webhook", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<OkHiWebhookResponse>> okhiWebhook(@RequestBody @Valid OkHiWebhookRequest request) {
        return ApiUtil.buildResponse(onboardingService.handleOkhiWebhook(request), HttpStatus.OK.toString(), "Request successful");
    }

    @Operation(summary = "Get Countries")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Allows Users to get all countries")
    })
    @PreAuthorize("hasRole('ROLE_users.get_countries')")
    @GetMapping(value = "/countries", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<Page<CountriesResponse>>> getCountries() {
        return ApiUtil.buildResponse(onboardingService.getCountries(), HttpStatus.OK.toString(), "Request successful");
    }

    @Operation(summary = "Get States")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Allows Users to get all states for a given country")
    })
    @PreAuthorize("hasRole('ROLE_users.get_states')")
    @GetMapping(value = "/states", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<Page<StatesResponse>>> getStates(@RequestParam() Long countryId) {
        return ApiUtil.buildResponse(onboardingService.getStates(countryId), HttpStatus.OK.toString(), "Request successful");
    }
}
