package org.meristem.oneapp.usersservice.controllers;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.meristem.oneapp.usersservice.constants.ApiConstants;
import org.meristem.oneapp.usersservice.domains.requests.SubmitOnboardingRequest;
import org.meristem.oneapp.usersservice.domains.responses.AppResponse;
import org.meristem.oneapp.usersservice.domains.responses.SubmitOnboardingResponse;
import org.meristem.oneapp.usersservice.domains.responses.UserOnboardingResponse;
import org.meristem.oneapp.usersservice.services.OnboardingService;
import org.meristem.oneapp.usersservice.utils.ApiUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<SubmitOnboardingResponse>> onboard(@RequestBody @Valid SubmitOnboardingRequest request) {
        return ApiUtil.buildResponse(onboardingService.onboard(request), HttpStatus.CREATED.toString(), "Onboarding flow processed");
    }

    @Operation(summary = "Get the onboarding flow")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get the onboarding flow details for a user ")
    })
    // TODO: REMOVE USER_ID AND REPLACE IT WITH THE ONE GOTTEN FROM THE LOGGED IN USER DETAILS
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<List<UserOnboardingResponse>>> onboard(@Parameter(example = "1", description = "Pass the id of the user.") @RequestParam(name = "userId") Long userId) {
        return ApiUtil.buildResponse(onboardingService.getOnboardingDetails(userId), HttpStatus.OK.toString(), "User onboarding details request successful");
    }
}
