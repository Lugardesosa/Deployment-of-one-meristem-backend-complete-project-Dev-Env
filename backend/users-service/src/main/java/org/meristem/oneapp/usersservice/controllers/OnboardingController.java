package org.meristem.oneapp.usersservice.controllers;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.meristem.oneapp.usersservice.constants.ApiConstants;
import org.meristem.oneapp.usersservice.domains.requests.SubmitOnboardingRequest;
import org.meristem.oneapp.usersservice.domains.requests.OnboardingRequest;
import org.meristem.oneapp.usersservice.domains.responses.AppResponse;
import org.meristem.oneapp.usersservice.domains.responses.SubmitOnboardingResponse;
import org.meristem.oneapp.usersservice.domains.responses.OnboardingResponse;
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

    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<OnboardingResponse>> onboard(@RequestBody @Valid OnboardingRequest request) {
        return ApiUtil.buildResponse(onboardingService.onboard(request), HttpStatus.CREATED.toString(), "User onboarded on the requested feature");
    }

    // TODO: REMOVE USER_ID AND REPLACE IT WITH THE ONE GOTTEN FROM THE LOGGED IN USER DETAILS
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<List<UserOnboardingResponse>>> onboard(@RequestParam(name = "userId") Long userId, @RequestParam(name = "featureId") Long featureId) {
        return ApiUtil.buildResponse(onboardingService.getOnboardingDetails(userId, featureId), HttpStatus.OK.toString(), "User onboarding details request successful");
    }

    @PostMapping(value = "/requirement/submit", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<SubmitOnboardingResponse>> submit(@RequestBody @Valid SubmitOnboardingRequest request) {
        return ApiUtil.buildResponse(onboardingService.complete(request), HttpStatus.OK.toString(), "Onboarding flow processed");
    }
}
