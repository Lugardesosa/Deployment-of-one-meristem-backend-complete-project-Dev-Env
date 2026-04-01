package org.meristem.oneapp.usersservice.services;

import jakarta.validation.Valid;
import org.jspecify.annotations.NonNull;
import org.meristem.oneapp.usersservice.config.MaskingUtils;
import org.meristem.oneapp.usersservice.constants.AppConstants;
import org.meristem.oneapp.usersservice.domains.enums.IdCardType;
import org.meristem.oneapp.usersservice.domains.enums.OnboardingRequirements;
import org.meristem.oneapp.usersservice.domains.enums.OnboardingStatus;
import org.meristem.oneapp.usersservice.domains.enums.UserOnboardingNotes;
import org.meristem.oneapp.usersservice.domains.requests.IdQueryRequest;
import org.meristem.oneapp.usersservice.domains.requests.NinValidationRequest;
import org.meristem.oneapp.usersservice.domains.requests.TaxIdQueryRequest;
import org.meristem.oneapp.usersservice.domains.responses.BvnQueryResponse;
import org.meristem.oneapp.usersservice.domains.responses.IdValidationResponse;
import org.meristem.oneapp.usersservice.domains.responses.TaxIdQueryResponse;
import org.meristem.oneapp.usersservice.dtos.IdQueryDetailsDto;
import org.meristem.oneapp.usersservice.exception.exceptions.BadRequestException;
import org.meristem.oneapp.usersservice.models.IdCard;
import org.meristem.oneapp.usersservice.models.InvestmentRequirement;
import org.meristem.oneapp.usersservice.models.UserIdDetails;
import org.meristem.oneapp.usersservice.models.Users;
import org.meristem.oneapp.usersservice.repositories.*;
import org.meristem.oneapp.usersservice.utils.AppUtil;
import org.meristem.oneapp.usersservice.utils.EncryptionUtil;
import org.meristem.oneapp.usersservice.utils.HashingUtil;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    TaxIdQueryResponse taxIdQuery(TaxIdQueryRequest request);

    IdValidationResponse validateNin(@Valid NinValidationRequest request);

    default BvnQueryResponse getBvnQueryResponse(CacheManager cacheManager, IdQueryRequest request, IdQueryDetailsDto dto,
                                                 EncryptionUtil encryptionUtil, HashingUtil hashingUtil, String idHashKey) {
        dto.setBvn(encryptionUtil.encrypt(request.idNumber()));
        dto.setBvnHashed(hashingUtil.hmacWithSha256(idHashKey, request.idNumber()));
        requireNonNull(cacheManager.getCache(AppConstants.SIGN_UP_CACHE_NAME)).put(dto.getBvnHashed(), dto);

        return BvnQueryResponse.builder().middleName(MaskingUtils.maskMiddleName(dto.getMiddleName()))
                .email(dto.getEmail()).firstName(MaskingUtils.maskFirstName(dto.getFirstName()))
                .lastName(MaskingUtils.maskLastName(dto.getLastName()))
                .phoneNumber(dto.getPhoneNumber())
                .build();
    }

    default IdValidationResponse compareNinAndBvnDetailsSaveAndReturn(Cache cache, UserIdDetails nin, List<String> names, UserIdDetails bvn, Users loggedInUser, RequirementsRepository requirementsRepository, UsersRepository usersRepository,
                                                                      UserOnboardingRepository userOnboardingRepository, IUsersService usersService, CustomRepository customRepository, IdCardRepository idCardRepository, String ninValue, String ninValueHashed, Long investmentId) {

        List<String> stringBuilder = new ArrayList<>();

        if (!AppUtil.dobMatch(nin.getDateOfBirth(), bvn.getDateOfBirth())) {
            stringBuilder.add("Date of births on NIN and BVN do not match");
        }

        List<String> ninNames = new ArrayList<>();
        ninNames.add(nin.getFirstName());
        ninNames.add(nin.getLastName());
        ninNames.add(nin.getMiddleName());
        if (!AppUtil.allNamesMatch(ninNames, names)) {
            stringBuilder.add("Names do not match");
        }

        // Validates user; updates onboarding status; notifies user service
        InvestmentRequirement requirements = requirementsRepository.findInvestmentRequirementsByRequirementName(OnboardingRequirements.NIN.getName(), investmentId);
        if (isNull(requirements)) {
            throw new BadRequestException("Action could not be completed");
        }
        // Validates user; updates onboarding status; notifies user service
        if (AppUtil.allNamesMatch(ninNames, names) && AppUtil.dobMatch(nin.getDateOfBirth(), bvn.getDateOfBirth())) {
            nin.setValidated(true);
            nin.setNote("NIN verified successfully");
            userOnboardingRepository.updateUserOnboardingStatus(loggedInUser.getId(), requirements.getId(), OnboardingStatus.APPROVED.getValue(), UserOnboardingNotes.APPROVED.note, true);
            usersService.completeUserOnboarding(usersRepository.getUserKyc2(loggedInUser.getEmail()), true, loggedInUser.getEmail(), investmentId, OnboardingRequirements.NIN);
            idCardRepository.findByIdCardTypeAndIdValueHashedAndUserId(IdCardType.NIN.getName(), ninValueHashed, loggedInUser.getId())
                    .ifPresentOrElse(id -> {
                            },
                            () -> {
                                idCardRepository.save(IdCard.builder().idValue(ninValue)
                                        .idCardType(IdCardType.NIN.getName()).accountType(loggedInUser.getAccountType())
                                        .userId(nin.getUserId()).idValueHashed(ninValueHashed)
                                        .build());
                            });
            cache.evict(loggedInUser.getEmail().concat(OnboardingRequirements.NIN.getName()));

        } else {
            usersService.resetUserOnboarding(loggedInUser.getEmail(), requirements.getId());
            nin.setValidated(false);
            if (!stringBuilder.isEmpty()) {
                stringBuilder.add("Please correct the mismatch and come back and revalidate.");
            }
            nin.setNote(stringBuilder.toString());
        }

        customRepository.save(nin);
        return IdValidationResponse.builder().message(nin.getNote()).success(nin.getValidated()).build();
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

    default BvnQueryResponse getBvnResponse(CacheManager cacheManager, EncryptionUtil encryptionUtil, IdQueryRequest request, IdCardRepository idCardRepository, HashingUtil hashingUtil, String idHashKey, UsersRepository usersRepository) {
        Optional<IdCard> idCard = idCardRepository.findByIdValueHashedAndIdCardType(hashingUtil.hmacWithSha256(idHashKey, request.idNumber()), IdCardType.BVN.getName());
        if (idCard.isPresent()) {

            IdQueryDetailsDto dto = usersRepository.findIdUserDetailById(idCard.get().getUserId());
            dto.setIdType(IdCardType.BVN.getName());
            return getBvnQueryResponse(cacheManager, request, dto, encryptionUtil, hashingUtil, idHashKey);
        }
        return null;
    }

    IdQueryDetailsDto ninQuery(String nin);
}
