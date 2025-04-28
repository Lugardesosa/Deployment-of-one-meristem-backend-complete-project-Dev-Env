package org.meristem.oneapp.usersservice.services;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.meristem.oneapp.kafka.dtos.MessageDetailsDto;
import org.meristem.oneapp.kafka.dtos.MessageDto;
import org.meristem.oneapp.usersservice.constants.AppConstants;
import org.meristem.oneapp.usersservice.constants.KafkaTopics;
import org.meristem.oneapp.usersservice.domains.enums.MessageMedium;
import org.meristem.oneapp.usersservice.domains.requests.SendOtpRequest;
import org.meristem.oneapp.usersservice.domains.enums.MessageType;
import org.meristem.oneapp.usersservice.domains.enums.OtpType;
import org.meristem.oneapp.usersservice.domains.requests.VerifyOtpRequest;
import org.meristem.oneapp.usersservice.domains.responses.SendOtpResponse;
import org.meristem.oneapp.usersservice.domains.responses.VerifyOtpResponse;
import org.meristem.oneapp.usersservice.exceptionHandler.exceptions.BadRequestException;
import org.meristem.oneapp.usersservice.exceptionHandler.exceptions.ResourceNotFoundException;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final KafkaSenderService<MessageDto> kafkaSenderService;
    private final OtpVerificationRepository otpVerificationRepository;
    private final UsersRepository usersRepository;

    @Transactional
    public SendOtpResponse sendOtp(SendOtpRequest sendOtpRequest) {
        MessageMedium messageMedium = validateAndGetMessageMedium(sendOtpRequest);

        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(AppConstants.OTP_EXPIRES_AT_MINUTES);

        if (sendOtpRequest.otpType().equals(OtpType.PASSWORD_RESET.getCode()) && !usersRepository.existsByEmailOrPhoneNumber(sendOtpRequest.recipient(), sendOtpRequest.recipient())) {
            return SendOtpResponse.builder().message("Successfully sent OTP").recipient(sendOtpRequest.recipient())
                    .timeToExpireInSeconds((int) ChronoUnit.SECONDS.between(LocalDateTime.now(), expiresAt)).build();
        }

        int code = AppUtil.randomInt(AppConstants.fourNumbersOtp.getFirst(), AppConstants.fourNumbersOtp.getSecond());

        // Expire old OTP for this user and the OTP type
        otpVerificationRepository.expireTimeByCode(LocalDateTime.now().minusMinutes(3), sendOtpRequest.recipient(), sendOtpRequest.otpType());

        OtpVerification otpVerification = OtpVerification.builder()
                .userId(sendOtpRequest.recipient()).expiresAt(expiresAt)
                .otpType(sendOtpRequest.otpType()).code(code).build();

        otpVerificationRepository.save(otpVerification);

        MessageDetailsDto messageDetailsDto = MessageDetailsDto.builder().recipient(new String[]{sendOtpRequest.recipient()})
                .body("This is the code " + otpVerification.getCode() + ".")
                .subject(OtpType.getMessageSubject(sendOtpRequest.otpType())).build();

        MessageDto messageDto = MessageDto.builder().medium(messageMedium).type(MessageType.OTP).message(messageDetailsDto).build();

        // TODO: DELETE the log statement
        log.info("OTP CODE ----> : {}", otpVerification.getCode());
        kafkaSenderService.send(messageDto, Map.of(KafkaHeaders.TOPIC, KafkaTopics.KAFKA_OTP_TOPIC));
        return SendOtpResponse.builder().message("Successfully sent OTP").recipient(sendOtpRequest.recipient())
                .timeToExpireInSeconds((int) ChronoUnit.SECONDS.between(LocalDateTime.now(), otpVerification.getExpiresAt()))
                .build();
    }

    private static MessageMedium validateAndGetMessageMedium(SendOtpRequest sendOtpRequest) {
        MessageMedium messageMedium = MessageMedium.of(sendOtpRequest.messageMedium());

        switch (messageMedium) {
            case EMAIL -> {
                if (!sendOtpRequest.recipient().matches(AppConstants.EMAIL_REGEX_PATTERN)) {
                    throw new BadRequestException("Invalid email format");
                }
            }
            case SMS, WHATSAPP -> {
                if (!sendOtpRequest.recipient().matches(AppConstants.PHONE_NG_REGEX_PATTERN)) {
                    throw new BadRequestException("Invalid phone number format");
                }
            }
            case null -> throw new BadRequestException("Invalid OTP type");
        }
        return messageMedium;
    }

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
