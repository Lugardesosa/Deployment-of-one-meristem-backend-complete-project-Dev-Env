package org.meristem.oneapp.usersservice.services;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.meristem.oneapp.kafka.dtos.OtpDto;
import org.meristem.oneapp.kafka.dtos.MessageDto;
import org.meristem.oneapp.usersservice.constants.AppConstants;
import org.meristem.oneapp.usersservice.constants.KafkaTopics;
import org.meristem.oneapp.usersservice.domains.enums.MessageMedium;
import org.meristem.oneapp.usersservice.domains.enums.MessageSubject;
import org.meristem.oneapp.usersservice.domains.enums.MessageType;
import org.meristem.oneapp.usersservice.domains.requests.SendOtpRequest;
import org.meristem.oneapp.usersservice.domains.requests.VerifyOtpRequest;
import org.meristem.oneapp.usersservice.domains.responses.SendOtpResponse;
import org.meristem.oneapp.usersservice.domains.responses.VerifyOtpResponse;
import org.meristem.oneapp.usersservice.exception.exceptions.BadRequestException;
import org.meristem.oneapp.usersservice.exception.exceptions.ResourceNotFoundException;
import org.meristem.oneapp.usersservice.models.OtpVerification;
import org.meristem.oneapp.usersservice.repositories.OtpVerificationRepository;
import org.meristem.oneapp.usersservice.repositories.UsersRepository;
import org.meristem.oneapp.usersservice.utils.AppUtil;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;


/**
 * Service class for handling notification-related operations, such as sending and verifying OTPs.
 * This class interacts with Kafka for message delivery and manages OTP verification logic.
 *
 * @author Kingsley
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final KafkaSenderService kafkaSenderService;
    private final OtpVerificationRepository otpVerificationRepository;
    private final UsersRepository usersRepository;


    /**
     * Sends an OTP to the specified recipient via the chosen medium (e.g., email, SMS, WhatsApp).
     * Validates the recipient's format and ensures old OTPs are expired before generating a new one.
     *
     * @param sendOtpRequest the request containing recipient details and OTP type
     * @return a {@link SendOtpResponse} containing the OTP expiration time and recipient details
     * @throws BadRequestException if the recipient format is invalid or OTP type is invalid
     */
    @Transactional
    public SendOtpResponse sendOtp(SendOtpRequest sendOtpRequest) {
        MessageMedium messageMedium = validateAndGetMessageMedium(sendOtpRequest.messageMedium(), sendOtpRequest.recipient());

        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(AppConstants.OTP_EXPIRES_AT_MINUTES);

        if (sendOtpRequest.otpType().equals(MessageSubject.PASSWORD_RESET.getCode()) && !usersRepository.existsByEmailOrPhoneNumber(sendOtpRequest.recipient(), sendOtpRequest.recipient())) {
            return SendOtpResponse.builder().message("Successfully sent OTP").recipient(sendOtpRequest.recipient())
                    .timeToExpireInSeconds((int) ChronoUnit.SECONDS.between(LocalDateTime.now(), expiresAt)).build();
        }

        if (sendOtpRequest.otpType().equals(MessageSubject.ONBOARDING_VERIFICATION.getCode())) {
            messageMedium = validateAndGetMessageMedium(MessageMedium.SMS.getValue(), sendOtpRequest.recipient());
        }

        int code = AppUtil.randomInt(AppConstants.fourNumbersOtp.getFirst(), AppConstants.fourNumbersOtp.getSecond());

        // Expire old OTP for this user and the OTP type
        otpVerificationRepository.expireTimeByCode(LocalDateTime.now().minusMinutes(3), sendOtpRequest.recipient(), sendOtpRequest.otpType());

        OtpVerification otpVerification = OtpVerification.builder()
                .userId(sendOtpRequest.recipient()).expiresAt(expiresAt)
                .otpType(sendOtpRequest.otpType()).code(code).build();

        otpVerificationRepository.save(otpVerification);

        OtpDto otpDto = OtpDto.builder().recipient(new String[]{sendOtpRequest.recipient()})
                .code(String.valueOf(otpVerification.getCode()))
                .subject(MessageSubject.getMessageSubject(sendOtpRequest.otpType())).build();

        MessageDto messageDto = MessageDto.builder().medium(messageMedium).type(MessageType.OTP).message(otpDto).classSimpleName(OtpDto.class.getSimpleName()).isHtml(messageMedium.equals(MessageMedium.EMAIL)).build();

        kafkaSenderService.send(messageDto, Map.of(KafkaHeaders.TOPIC, KafkaTopics.KAFKA_OTP_TOPIC));
        return SendOtpResponse.builder().message("Successfully sent OTP").recipient(sendOtpRequest.recipient())
                .timeToExpireInSeconds((int) ChronoUnit.SECONDS.between(LocalDateTime.now(), otpVerification.getExpiresAt()))
                .build();
    }

    /**
     * Validates the recipient's format based on the message medium and returns the corresponding {@link MessageMedium}.
     *
     * @param requestMedium the request message medium
     * @param recipient the request recipient
     * @return the validated {@link MessageMedium}
     * @throws BadRequestException if the recipient format is invalid or OTP type is invalid
     */
    private static MessageMedium validateAndGetMessageMedium(Integer requestMedium, String recipient) {
        MessageMedium messageMedium = MessageMedium.of(requestMedium);

        switch (messageMedium) {
            case EMAIL -> {
                if (!recipient.matches(AppConstants.EMAIL_REGEX_PATTERN)) {
                    throw new BadRequestException("Invalid email format");
                }
            }
            case SMS, WHATSAPP -> {
                if (!recipient.matches(AppConstants.PHONE_NG_REGEX_PATTERN)) {
                    throw new BadRequestException("Invalid phone number format");
                }
            }
            case null -> throw new BadRequestException("Invalid OTP type");
        }
        return messageMedium;
    }

    /**
     * Verifies the provided OTP for the specified recipient and OTP type.
     * Ensures the OTP is not expired or already used.
     *
     * @param request the request containing OTP details and recipient information
     * @return a {@link VerifyOtpResponse} indicating the verification status
     * @throws ResourceNotFoundException if the OTP is not found
     */
    public VerifyOtpResponse verifyOtp(@Valid VerifyOtpRequest request) {

        OtpVerification otpVerification = otpVerificationRepository.findByOtpTypeAndCodeAndUserId(request.otpType(), request.otp(), request.recipient())
                .orElseThrow(() -> new ResourceNotFoundException("OTP not found", "OTP", request.otp().toString()));

        if (otpVerification.getVerified()) {
            return VerifyOtpResponse.builder().status(false).message("OTP already used").build();
        }
        if (otpVerification.getExpiresAt().isBefore(LocalDateTime.now())) {
            return VerifyOtpResponse.builder().status(false).message("OTP expired").build();
        }
        otpVerification.setVerified(true);
        otpVerificationRepository.save(otpVerification);
        return VerifyOtpResponse.builder().status(true).message("OTP verified").build();
    }
}
