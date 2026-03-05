package org.meristem.oneapp.usersservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.jspecify.annotations.NonNull;
import org.meristem.oneapp.kafka.dtos.EmailConfirmationDto;
import org.meristem.oneapp.kafka.dtos.MessageDto;
import org.meristem.oneapp.kafka.dtos.OtpDto;
import org.meristem.oneapp.usersservice.constants.AppConstants;
import org.meristem.oneapp.usersservice.constants.KafkaTopics;
import org.meristem.oneapp.usersservice.domains.enums.*;
import org.meristem.oneapp.usersservice.domains.requests.IdQueryRequest;
import org.meristem.oneapp.usersservice.domains.requests.SendOtpRequest;
import org.meristem.oneapp.usersservice.domains.responses.BvnQueryResponse;
import org.meristem.oneapp.usersservice.domains.responses.IdValidationResponse;
import org.meristem.oneapp.usersservice.dtos.IdQueryDetailsDto;
import org.meristem.oneapp.usersservice.dtos.OtpVerificationDto;
import org.meristem.oneapp.usersservice.exception.exceptions.BadRequestException;
import org.meristem.oneapp.usersservice.integrations.MiddleWareClient;
import org.meristem.oneapp.usersservice.integrations.responses.MiddlewareCustomerResponse;
import org.meristem.oneapp.usersservice.models.*;
import org.meristem.oneapp.usersservice.repositories.*;
import org.meristem.oneapp.usersservice.services.implementations.OtpService;
import org.meristem.oneapp.usersservice.utils.AppUtil;
import org.meristem.oneapp.usersservice.utils.EncryptionUtil;
import org.meristem.oneapp.usersservice.utils.HashingUtil;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;

/**
 * Interface for handling Dojah related operations.
 * Provides functionality for BVN queries, smart link generation, and webhook processing.
 */
public interface IKycService {

    /**
     * Performs a BVN query through Smile ID's enhanced KYC service.
     *
     * @param request the BVN query request
     * @return a {@link BvnQueryResponse} containing user details
     */
    BvnQueryResponse bvnQuery(IdQueryRequest request);

    IdValidationResponse bvnValidation(MultipartFile file);

    IdValidationResponse validateNin(@Valid IdQueryRequest request);

    default BvnQueryResponse getBvnQueryResponse(CacheManager cacheManager, IdQueryRequest request, IdQueryDetailsDto dto,
                                                 EncryptionUtil encryptionUtil, HashingUtil hashingUtil, String idHashKey) {
        dto.setBvn(encryptionUtil.encrypt(request.idNumber()));
        dto.setBvnHashed(hashingUtil.hmacWithSha256(idHashKey, request.idNumber()));
        requireNonNull(cacheManager.getCache(AppConstants.SIGN_UP_CACHE_NAME)).put(dto.getBvnHashed(), dto);

        return BvnQueryResponse.builder().middleName(dto.getMiddleName())
                .email(dto.getEmail()).firstName(dto.getFirstName()).lastName(dto.getLastName())
                .phoneNumber(dto.getPhoneNumber()).build();
    }

    @NonNull
    default List<String> buildNames(UserIdDetails bvn) {
        List<String> names = new ArrayList<>();
        names.add(bvn.getFirstName());
        names.add(bvn.getLastName());
        names.add(bvn.getMiddleName());
        return names;
    }

    default IdValidationResponse compareNinAndBvnDetailsSaveAndReturn(Cache cache, UserIdDetails nin, List<String> names, UserIdDetails bvn, Users loggedInUser, RequirementsRepository requirementsRepository,
                                                                      UserOnboardingRepository userOnboardingRepository, IUsersService usersService, CustomRepository customRepository, IdCardRepository idCardRepository, String ninValue, String ninValueHashed) {

        StringBuilder stringBuilder = new StringBuilder();

        if (!firstNamesMatch(nin, names)) {
            stringBuilder.append("Firstnames on NIN and BVN do not match,");
        }
        if (!lastNamesMatch(nin, names)) {
            stringBuilder.append("Lastnames on NIN and BVN do not match,");
        }
        if (!middleNamesMatch(nin, names)) {
            stringBuilder.append("Middle names on NIN and BVN do not match,");
        }
        if (!dobMatch(nin, bvn)) {
            stringBuilder.append("Date of births on NIN and BVN do not match,");
        }

        // Validates user; updates onboarding status; notifies user service
        Requirements requirements = requirementsRepository.findRequirementsByRequirementName(OnboardingRequirements.NIN.getName());
        // Validates user; updates onboarding status; notifies user service
        if (firstNamesMatch(nin, names) && lastNamesMatch(nin, names) && dobMatch(nin, bvn) && middleNamesMatch(nin, names)) {
            nin.setValidated(true);
            nin.setNote("NIN verified successfully");
            userOnboardingRepository.updateUserOnboardingStatus(loggedInUser.getId(), requirements.getId(), OnboardingStatus.APPROVED.getValue(), UserOnboardingNotes.APPROVED.note, true);
            usersService.completeUserOnboarding(loggedInUser.getEmail());
            idCardRepository.save(IdCard.builder().idValue(ninValue)
                    .idCardType(IdCardType.NIN.getName())
                    .userId(nin.getUserId()).idValueHashed(ninValueHashed)
                    .build());
            cache.evict(loggedInUser.getEmail().concat(OnboardingRequirements.BVN.getName()));

        } else {
            usersService.resetUserOnboarding(loggedInUser.getEmail(), requirements.getId());
            nin.setValidated(false);
            nin.setNote(stringBuilder.isEmpty() ? null : stringBuilder.toString().concat("Please correct the mismatch and come back and revalidate."));
        }

        customRepository.save(nin);
        return IdValidationResponse.builder().message(nin.getNote()).success(nin.getValidated()).build();
    }

    default boolean firstNamesMatch(UserIdDetails uid, List<String> names) {
        return names.stream().anyMatch(n -> n.equalsIgnoreCase(uid.getFirstName()));
    }

    default boolean lastNamesMatch(UserIdDetails uid, List<String> names) {
        return names.stream().anyMatch(n -> n.equalsIgnoreCase(uid.getLastName()));

    }

    default boolean middleNamesMatch(UserIdDetails uid, List<String> names) {
        return names.stream().anyMatch(n -> n.equalsIgnoreCase(uid.getMiddleName()));
    }

    /**
     * Checks if user date of birth matches date of birth sent
     */
    default boolean dobMatch(UserIdDetails uid, UserIdDetails bvn) {

        return AppUtil.nonIsNull(uid.getDateOfBirth(), bvn.getDateOfBirth()) &&
                uid.getDateOfBirth().isEqual(bvn.getDateOfBirth());
    }

    default @NonNull Cache getIdQueryCache(CacheManager cacheManager) {
        Cache cache = cacheManager.getCache(AppConstants.ID_VERIFICATION_CACHE_NAME);

        String loggedInUserEmail = AppUtil.getLoggedInUserEmail();
        assert cache != null;
        LocalDateTime expireIn = cache.get(loggedInUserEmail.concat(OnboardingRequirements.NIN.getName()), LocalDateTime.class);

        if (isNull(expireIn)) {
            expireIn = LocalDateTime.now().plusMinutes(AppConstants.ID_VERIFICATION_CACHE_EXPIRES_IN);
            cache.put(loggedInUserEmail.concat(OnboardingRequirements.NIN.getName()), expireIn);
        } else if (expireIn.isAfter(LocalDateTime.now())) {
            throw new BadRequestException("Try again at " + DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(expireIn));
        }
        return cache;
    }
}
