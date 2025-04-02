package org.meristem.oneapp.usersservice.services;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.meristem.oneapp.kafka.dtos.MessageDetailsDto;
import org.meristem.oneapp.kafka.dtos.MessageDto;
import org.meristem.oneapp.usersservice.constants.AppConstants;
import org.meristem.oneapp.usersservice.domains.enums.MessageMedium;
import org.meristem.oneapp.kafka.dtos.SendOtpRequest;
import org.meristem.oneapp.usersservice.domains.enums.MessageType;
import org.meristem.oneapp.usersservice.domains.enums.OtpType;
import org.meristem.oneapp.usersservice.domains.requests.VerifyOtpRequest;
import org.meristem.oneapp.usersservice.domains.responses.SendOtpResponse;
import org.meristem.oneapp.usersservice.domains.responses.VerifyOtpResponse;
import org.meristem.oneapp.usersservice.exceptionHandler.exceptions.BadRequestException;
import org.meristem.oneapp.usersservice.exceptionHandler.exceptions.ResourceNotFoundException;
import org.meristem.oneapp.usersservice.models.OtpVerification;
import org.meristem.oneapp.usersservice.repositories.OtpVerificationRepository;
import org.meristem.oneapp.usersservice.utils.AppUtil;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final KafkaSenderService<MessageDto> kafkaSenderService;
    private final OtpVerificationRepository otpVerificationRepository;

    @Transactional
    public SendOtpResponse sendOtp(SendOtpRequest sendOtpRequest) {
        MessageMedium messageMedium = validateAndGetMessageMedium(sendOtpRequest);
        int code = AppUtil.randomInt(1000000, 9000000);

        otpVerificationRepository.expireTimeByCode(LocalDateTime.now().minusMinutes(3), sendOtpRequest.recipient(), MessageType.OTP.getValue());

        OtpVerification otpVerification = OtpVerification.builder()
                .userId(sendOtpRequest.recipient()).expiresAt(LocalDateTime.now().plusMinutes(AppConstants.OTP_EXPIRES_AT_MINUTES))
                .otpType(OtpType.REGISTRATION.getValue()).code(code).build();

        otpVerificationRepository.save(otpVerification);

        MessageDetailsDto messageDetailsDto = MessageDetailsDto.builder().recipient(new String[]{sendOtpRequest.recipient()})
                .body("This is the code " + otpVerification.getCode() + ".").subject("Registration Otp Mail").build();
        MessageDto messageDto = MessageDto.builder().medium(messageMedium).type(MessageType.OTP).message(messageDetailsDto).build();

        // TODO: DELETE the log statement
        log.info("----> CODE: {}", otpVerification.getCode());
        kafkaSenderService.send(messageDto, Map.of(KafkaHeaders.TOPIC, AppConstants.KAFKA_OTP_TOPIC));
        return SendOtpResponse.builder().message("Successfully sent OTP").recipient(sendOtpRequest.recipient()).build();
    }

    private static MessageMedium validateAndGetMessageMedium(SendOtpRequest sendOtpRequest) {
        MessageMedium messageMedium = MessageMedium.of(sendOtpRequest.otpType());

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
            case null -> throw new BadRequestException("Invalid otp type");
        }
        return messageMedium;
    }

    public VerifyOtpResponse verifyOtp(@Valid VerifyOtpRequest request) {

        OtpVerification otpVerification = otpVerificationRepository.findByOtpTypeAndCodeAndUserId(request.otpType(), request.otp(), request.recipient())
                .orElseThrow(() -> new ResourceNotFoundException("Otp not found", "otp", request.otp().toString()));

        if (otpVerification.getVerified()) {
            return VerifyOtpResponse.builder().status(false).message("Otp already used").build();
        }
        if (otpVerification.getExpiresAt().isBefore(LocalDateTime.now())) {
            return VerifyOtpResponse.builder().status(false).message("Otp expired").build();
        }
        otpVerification.setVerified(true);
        otpVerificationRepository.save(otpVerification);
        return VerifyOtpResponse.builder().status(true).message("Otp verified").build();
    }
}
