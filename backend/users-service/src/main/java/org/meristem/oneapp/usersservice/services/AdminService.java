package org.meristem.oneapp.usersservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.meristem.oneapp.usersservice.constants.AppConstants;
import org.meristem.oneapp.usersservice.domains.requests.CreateNextOfKinRequest;
import org.meristem.oneapp.usersservice.domains.requests.DobRequest;
import org.meristem.oneapp.usersservice.domains.requests.GenderRequest;
import org.meristem.oneapp.usersservice.domains.responses.DobResponse;
import org.meristem.oneapp.usersservice.domains.responses.GenderResponse;
import org.meristem.oneapp.usersservice.domains.responses.NextOfKinResponse;
import org.meristem.oneapp.usersservice.repositories.UserProfileRepository;
import org.meristem.oneapp.usersservice.repositories.UsersRepository;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import static java.util.Objects.requireNonNull;


/**
 * Service class for managing administrative operations.
 * Provides functionality for creating admin users and updating next-of-kin details for users.
 * This service ensures that admin users are created with appropriate roles and notifies them of their credentials.
 * It also includes a placeholder for updating next-of-kin details, which is restricted to admin users.
 * Note: The `updateNextOfKin` method is yet to be implemented.
 *
 * @author Kingsley
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class AdminService {

    private final UsersRepository usersRepository;
    private final CacheManager cacheManager;
    private final UserProfileRepository userProfileRepository;


    /**
     * Updates the next-of-kin details for a user.
     * This method is restricted to admin users and is currently a placeholder for future implementation.
     *
     * @param request the request containing next-of-kin details
     * @return a {@link NextOfKinResponse} containing the updated next-of-kin details
     */
    // TODO: COMPLETE THIS METHOD
    // ALLOW ADMINS UPDATE USER'S NEXT IF KIN
    public NextOfKinResponse updateNextOfKin(CreateNextOfKinRequest request) {

        return NextOfKinResponse.builder().build();
    }

    public DobResponse updateDob(DobRequest request) {
        int updated = userProfileRepository.updateDob(request.userId(), request.dob());
        requireNonNull(cacheManager.getCache(AppConstants.USERS_CACHE_NAME)).evict(usersRepository.findEmailById(request.userId()));
        return DobResponse.builder().status(updated > 0).message(updated > 0 ? "Dob successfully updated." : "Invalid id passed").build();
    }

    public GenderResponse updateGender(GenderRequest request) {
        int updated = userProfileRepository.updateGender(request.userId(), request.gender().name());
        requireNonNull(cacheManager.getCache(AppConstants.USERS_CACHE_NAME)).evict(usersRepository.findEmailById(request.userId()));
        return GenderResponse.builder().status(updated > 0).message(updated > 0 ? "Dob successfully updated." : "Invalid id passed").build();
    }
}
