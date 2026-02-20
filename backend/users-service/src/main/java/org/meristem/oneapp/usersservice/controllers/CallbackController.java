package org.meristem.oneapp.usersservice.controllers;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.meristem.oneapp.usersservice.constants.ApiConstants;
import org.meristem.oneapp.usersservice.domains.requests.OkHiWebhookRequest;
import org.meristem.oneapp.usersservice.domains.requests.PastelAmlWebhookRequest;
import org.meristem.oneapp.usersservice.domains.responses.AppResponse;
import org.meristem.oneapp.usersservice.domains.responses.SmileIdWebhookNotification;
import org.meristem.oneapp.usersservice.domains.responses.SmileIdWebhookResponse;
import org.meristem.oneapp.usersservice.domains.responses.WebhookResponse;
import org.meristem.oneapp.usersservice.services.IAmlService;
import org.meristem.oneapp.usersservice.services.IKycService;
import org.meristem.oneapp.usersservice.services.implementations.OnboardingService;
import org.meristem.oneapp.usersservice.utils.ApiUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiConstants.CONTEXT_PATH + "callback")
public class CallbackController {

    private final IKycService smileIdService;
    private final IKycService dojahService;
    private final OnboardingService onboardingService;
    private final IAmlService amlService;

    public CallbackController(@Qualifier("SMILE_ID") IKycService smileIdService, @Qualifier("DOJAH") IKycService dojahService, OnboardingService onboardingService, IAmlService amlService) {
        this.smileIdService = smileIdService;
        this.dojahService = dojahService;
        this.onboardingService = onboardingService;
        this.amlService = amlService;
    }

    @Operation(summary = "Smile Id webhook")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Allows Smile Id to send webhook notifications to us")
    })
    @PostMapping(value = "/smile-id", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<SmileIdWebhookResponse>> smileIdWebhook(@RequestBody @Valid SmileIdWebhookNotification request) {
        return ApiUtil.buildResponse(smileIdService.handleWebhook(request), HttpStatus.OK.toString(), "Request successful");
    }

    @Operation(summary = "Smile Id webhook")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Allows Dojah to send webhook notifications to us")
    })
    @PostMapping(value = "/dojah", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<SmileIdWebhookResponse>> dojahWebhook(@RequestBody @Valid SmileIdWebhookNotification request) {
        return ApiUtil.buildResponse(dojahService.handleWebhook(request), HttpStatus.OK.toString(), "Request successful");
    }

    @Operation(summary = "Ok Hi webhook")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Allows OkHi to send webhook notifications to us")
    })
    @PostMapping(value = "/okhi", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<WebhookResponse>> okhiWebhook(@RequestBody @Valid OkHiWebhookRequest request) {
        return ApiUtil.buildResponse(onboardingService.handleOkhiWebhook(request), HttpStatus.OK.toString(), "Request successful");
    }

    @Operation(summary = "Ok Hi webhook")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Allows Pastel to send webhook notifications to us")
    })
    @PostMapping(value = "/pastel", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<WebhookResponse>> pastelWebhook(@RequestBody PastelAmlWebhookRequest request) {
        return ApiUtil.buildResponse(amlService.handleAmlWebhook(request), HttpStatus.OK.toString(), "Request successful");
    }
}
