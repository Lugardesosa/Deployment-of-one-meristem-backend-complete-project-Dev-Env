package org.meristem.oneapp.usersservice.services;

import jakarta.validation.Valid;
import org.meristem.oneapp.usersservice.domains.requests.SendOtpRequest;
import org.meristem.oneapp.usersservice.domains.requests.VerifyOtpRequest;
import org.meristem.oneapp.usersservice.domains.responses.SendOtpResponse;
import org.meristem.oneapp.usersservice.domains.responses.VerifyOtpResponse;

/**
 * Interface for handling OTP-related operations.
 * Provides functionality for sending and verifying OTPs.
 *
 * @author Kingsley
 */
public interface IOtpService {

    /**
     * Sends an OTP to the specified recipient via the chosen medium.
     *
     * @param sendOtpRequest the request containing recipient details and OTP type
     * @return a {@link SendOtpResponse} containing the OTP expiration time and recipient details
     */
    SendOtpResponse sendOtp(SendOtpRequest sendOtpRequest, String cacheKey);
    SendOtpResponse sendOtp(SendOtpRequest sendOtpRequest);

    /**
     * Verifies the provided OTP for the specified recipient and OTP type.
     *
     * @param request the request containing OTP details and recipient information
     * @return a {@link VerifyOtpResponse} indicating the verification status
     */
    VerifyOtpResponse verifyOtp(@Valid VerifyOtpRequest request, String cacheKey);
    VerifyOtpResponse verifyOtp(@Valid VerifyOtpRequest request);
}
