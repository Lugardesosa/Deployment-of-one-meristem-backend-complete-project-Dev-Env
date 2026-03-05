package org.meristem.oneapp.usersservice.services.implementations;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.meristem.oneapp.kafka.dtos.MessageDto;
import org.meristem.oneapp.kafka.dtos.OtpDto;
import org.meristem.oneapp.usersservice.constants.AppConstants;
import org.meristem.oneapp.usersservice.constants.KafkaTopics;
import org.meristem.oneapp.usersservice.domains.enums.*;
import org.meristem.oneapp.usersservice.domains.requests.SendOtpRequest;
import org.meristem.oneapp.usersservice.domains.requests.VerifyOtpRequest;
import org.meristem.oneapp.usersservice.domains.responses.SendOtpResponse;
import org.meristem.oneapp.usersservice.domains.responses.VerifyOtpResponse;
import org.meristem.oneapp.usersservice.exception.exceptions.BadRequestException;
import org.meristem.oneapp.usersservice.exception.exceptions.ResourceNotFoundException;
import org.meristem.oneapp.usersservice.dtos.OtpVerificationDto;
import org.meristem.oneapp.usersservice.models.OutboxEvent;
import org.meristem.oneapp.usersservice.repositories.OutboxEventRepository;
import org.meristem.oneapp.usersservice.repositories.UsersRepository;
import org.meristem.oneapp.usersservice.services.IOtpService;
import org.meristem.oneapp.usersservice.utils.AppUtil;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static java.util.Objects.*;


/**
 * Service class for handling notification-related operations, such as sending and verifying OTPs.
 * This class interacts with Kafka for message delivery and manages OTP verification logic.
 *
 * @author Kingsley
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OtpService implements IOtpService {

    private final UsersRepository usersRepository;
    private final CacheManager cacheManager;
    private final ObjectMapper objectMapper;
    private final OutboxEventRepository outboxEventRepository;


    /**
     * Sends an OTP to the specified recipient via the chosen medium (e.g., email, SMS, WhatsApp).
     * Validates the recipient's format and ensures old OTPs are expired before generating a new one.
     *
     * @param sendOtpRequest the request containing recipient details and OTP type
     * @return a {@link SendOtpResponse} containing the OTP expiration time and recipient details
     * @throws BadRequestException if the recipient format is invalid or OTP type is invalid
     */
    @Transactional
    @Override
    public SendOtpResponse sendOtp(SendOtpRequest sendOtpRequest, String cacheKey) {

        Cache cache = requireNonNull(cacheManager.getCache(AppConstants.OTP_CACHE_NAME), "Error creating otp");
        MessageMedium messageMedium = validateAndGetMessageMedium(sendOtpRequest.messageMedium(), sendOtpRequest.recipient());

        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(AppConstants.OTP_EXPIRES_AT_MINUTES);

        if (sendOtpRequest.otpType().equals(MessageSubject.PASSWORD_RESET.getCode()) && !usersRepository.existsByEmailOrPhoneNumber(sendOtpRequest.recipient(), sendOtpRequest.recipient())) {
            return SendOtpResponse.builder().message("Successfully sent OTP").recipient(sendOtpRequest.recipient())
                    .timeToExpireInSeconds((int) ChronoUnit.SECONDS.between(LocalDateTime.now(), expiresAt)).build();
        }

        if (sendOtpRequest.otpType().equals(MessageSubject.ONBOARDING_VERIFICATION.getCode())) {
            messageMedium = validateAndGetMessageMedium(MessageMedium.SMS.getValue(), sendOtpRequest.recipient());
        }

        OtpVerificationDto otpVerificationDto = generateOtpVerificationDto(sendOtpRequest, expiresAt, cache, cacheKey);

        OtpDto otpDto = OtpDto.builder().recipient(new String[]{sendOtpRequest.recipient()})
                .code(String.valueOf(otpVerificationDto.getCode()))
                .subject(MessageSubject.getMessageSubject(sendOtpRequest.otpType())).build();

        MessageDto messageDto = MessageDto.builder().medium(messageMedium).type(MessageType.OTP).message(otpDto).classSimpleName(OtpDto.class.getSimpleName()).isHtml(messageMedium.equals(MessageMedium.EMAIL)).build();

        try {

        OutboxEvent otpMessage = OutboxEvent.builder()
                .aggregateId(0L).aggregateType(AggregateType.OTP.getValue())
                .eventType(KafkaTopics.KAFKA_OTP_TOPIC)
                .outboxStatus(OutboxStatus.PENDING.getValue())
                .eventClass(MessageDto.class.getName())
                .eventKey(sendOtpRequest.recipient())
                .payload(objectMapper.writeValueAsString(messageDto)).build();
        outboxEventRepository.save(otpMessage);
        } catch (JsonProcessingException e) {
            log.error("Error creating otp for user with email/phone {} to outbox", sendOtpRequest.recipient(), e);
            throw new RuntimeException("Please try again later");
        }
        return SendOtpResponse.builder().message("Successfully sent OTP").recipient(sendOtpRequest.recipient())
                .timeToExpireInSeconds((int) ChronoUnit.SECONDS.between(LocalDateTime.now(), otpVerificationDto.getExpiresAt()))
                .build();
    }

    @Transactional
    @Override
    public SendOtpResponse sendOtp(SendOtpRequest sendOtpRequest) {
        return sendOtp(sendOtpRequest, null);
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
    @Transactional
    @Override
    public VerifyOtpResponse verifyOtp(VerifyOtpRequest request, String cacheKey) {

        Cache cache = requireNonNull(cacheManager.getCache(AppConstants.OTP_CACHE_NAME), "Error getting otp");
        cacheKey = StringUtils.isNotBlank(cacheKey) ? cacheKey : request.recipient();

        cacheKey = cacheKey.concat(request.otpType().toString());
        OtpVerificationDto otpVerificationDto = cache.get(cacheKey, OtpVerificationDto.class);

        if (isNull(otpVerificationDto)) {
            throw new ResourceNotFoundException("OTP not found", "OTP", request.otp().toString());
        }

        if (otpVerificationDto.getVerified()) {
            return VerifyOtpResponse.builder().status(false).message("OTP already used").build();
        }

        if (!otpVerificationDto.getOtpType().equals(request.otpType())) {
            return VerifyOtpResponse.builder().status(false).message("Invalid otp type").build();
        }

        if (!otpVerificationDto.getCode().equals(request.otp())) {
            return VerifyOtpResponse.builder().status(false).message("Invalid code").build();
        }

        if (otpVerificationDto.getExpiresAt().isBefore(LocalDateTime.now())) {
            return VerifyOtpResponse.builder().status(false).message("OTP expired").build();
        }
        otpVerificationDto.setVerified(true);

        cache.put(cacheKey, otpVerificationDto);

        return VerifyOtpResponse.builder().status(true).message("OTP verified").build();
    }

    @Override
    public VerifyOtpResponse verifyOtp(VerifyOtpRequest request) {
        return verifyOtp(request, null);
    }

    public static OtpVerificationDto generateOtpVerificationDto(SendOtpRequest sendOtpRequest, LocalDateTime expiresAt, Cache cache, String cacheKey) {
        int code = AppUtil.randomInt(AppConstants.fourNumbersOtp.getFirst(), AppConstants.fourNumbersOtp.getSecond());

        OtpVerificationDto otpVerificationDto = OtpVerificationDto.builder()
                .userId(sendOtpRequest.recipient()).expiresAt(expiresAt)
                .otpType(sendOtpRequest.otpType()).code(code).build();

        cacheKey = StringUtils.isNotBlank(cacheKey) ? cacheKey : sendOtpRequest.recipient();
        cache.put(cacheKey.concat(sendOtpRequest.otpType().toString()), otpVerificationDto);
        return otpVerificationDto;
    }
}
