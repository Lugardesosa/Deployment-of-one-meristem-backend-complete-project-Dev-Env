package org.meristem.oneapp.usersservice.controllers;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.meristem.oneapp.usersservice.constants.ApiConstants;
import org.meristem.oneapp.kafka.dtos.SendOtpRequest;
import org.meristem.oneapp.usersservice.domains.requests.VerifyOtpRequest;
import org.meristem.oneapp.usersservice.domains.responses.AppResponse;
import org.meristem.oneapp.usersservice.domains.responses.SendOtpResponse;
import org.meristem.oneapp.usersservice.domains.responses.VerifyOtpResponse;
import org.meristem.oneapp.usersservice.services.NotificationService;
import org.meristem.oneapp.usersservice.utils.ApiUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiConstants.CONTEXT_PATH + "notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping(value = "/otp", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<SendOtpResponse>> sendOtp(@Valid @RequestBody SendOtpRequest sendOtpRequest) {
        return ApiUtil.buildResponse(notificationService.sendOtp(sendOtpRequest), HttpStatus.OK.toString(), "Otp sent to ".concat(sendOtpRequest.recipient()));
    }

    @PostMapping(value = "/otp/verify", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<VerifyOtpResponse>> verifyOtp(@Valid @RequestBody VerifyOtpRequest verifyOtpRequest) {
        return ApiUtil.buildResponse(notificationService.verifyOtp(verifyOtpRequest), HttpStatus.OK.toString(), "Otp request verification processed.");
    }
}
