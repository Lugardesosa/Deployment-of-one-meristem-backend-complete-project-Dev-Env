package org.meristem.oneapp.usersservice.services;

import jakarta.validation.Valid;
import org.jspecify.annotations.NonNull;
import org.meristem.oneapp.usersservice.constants.AppConstants;
import org.meristem.oneapp.usersservice.domains.enums.OnboardingRequirements;
import org.meristem.oneapp.usersservice.domains.enums.OnboardingStatus;
import org.meristem.oneapp.usersservice.domains.enums.UserOnboardingNotes;
import org.meristem.oneapp.usersservice.domains.requests.IdQueryRequest;
import org.meristem.oneapp.usersservice.domains.responses.BvnQueryResponse;
import org.meristem.oneapp.usersservice.domains.responses.NinValidationResponse;
import org.meristem.oneapp.usersservice.dtos.IdQueryDetailsDto;
import org.meristem.oneapp.usersservice.integrations.responses.DojahBvnLookUpResponse;
import org.meristem.oneapp.usersservice.models.Requirements;
import org.meristem.oneapp.usersservice.models.UserIdDetails;
import org.meristem.oneapp.usersservice.models.Users;
import org.meristem.oneapp.usersservice.repositories.CustomRepository;
import org.meristem.oneapp.usersservice.repositories.RequirementsRepository;
import org.meristem.oneapp.usersservice.repositories.UserOnboardingRepository;
import org.meristem.oneapp.usersservice.utils.AppUtil;
import org.meristem.oneapp.usersservice.utils.EncryptionUtil;
import org.meristem.oneapp.usersservice.utils.HashingUtil;
import org.springframework.cache.CacheManager;

import java.util.ArrayList;
import java.util.List;

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

    NinValidationResponse validateNin(@Valid IdQueryRequest request);

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

    default NinValidationResponse compareNinAndBvnDetailsSaveAndReturn(UserIdDetails nin, List<String> names, UserIdDetails bvn, Users loggedInUser, RequirementsRepository requirementsRepository,
                                                                       UserOnboardingRepository userOnboardingRepository, IUsersService usersService, CustomRepository customRepository) {

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
        } else {
            usersService.resetUserOnboarding(loggedInUser.getEmail(), requirements.getId());
            nin.setValidated(false);
            nin.setNote(stringBuilder.isEmpty() ? null : stringBuilder.toString().concat("Please correct the mismatch and come back and revalidate."));
        }

        customRepository.save(nin);
        return NinValidationResponse.builder().message(nin.getNote()).success(nin.getValidated()).build();
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

}
