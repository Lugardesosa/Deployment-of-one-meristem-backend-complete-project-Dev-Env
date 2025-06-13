package org.meristem.oneapp.usersservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.meristem.oneapp.kafka.dtos.MessageDetailsDto;
import org.meristem.oneapp.kafka.dtos.MessageDto;
import org.meristem.oneapp.usersservice.constants.AppConstants;
import org.meristem.oneapp.usersservice.constants.KafkaTopics;
import org.meristem.oneapp.usersservice.constants.MessageSubjects;
import org.meristem.oneapp.usersservice.domains.enums.MessageMedium;
import org.meristem.oneapp.usersservice.domains.enums.MessageType;
import org.meristem.oneapp.usersservice.domains.enums.Roles;
import org.meristem.oneapp.usersservice.domains.requests.CreateAdminRequest;
import org.meristem.oneapp.usersservice.domains.requests.CreateNextOfKinRequest;
import org.meristem.oneapp.usersservice.domains.requests.DobRequest;
import org.meristem.oneapp.usersservice.domains.requests.GenderRequest;
import org.meristem.oneapp.usersservice.domains.responses.DobResponse;
import org.meristem.oneapp.usersservice.domains.responses.GenderResponse;
import org.meristem.oneapp.usersservice.domains.responses.NextOfKinResponse;
import org.meristem.oneapp.usersservice.domains.responses.UsersResponse;
import org.meristem.oneapp.usersservice.mappers.UsersMapping;
import org.meristem.oneapp.usersservice.models.Users;
import org.meristem.oneapp.usersservice.repositories.RolesRepository;
import org.meristem.oneapp.usersservice.repositories.UserProfileRepository;
import org.meristem.oneapp.usersservice.repositories.UsersRepository;
import org.meristem.oneapp.usersservice.utils.AppUtil;
import org.springframework.cache.CacheManager;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

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
    private final UsersMapping usersMapper = UsersMapping.INSTANCE;
    private final KafkaSenderService kafkaSenderService;
    private final PasswordEncoder passwordEncoder;
    private final RolesRepository rolesRepository;
    private final CacheManager cacheManager;
    private final UserProfileRepository userProfileRepository;


    /**
     * Creates a new admin user with a randomly generated password.
     * Assigns the admin role to the user and notifies them of their credentials via email.
     *
     * @param request the request containing admin user details
     * @return a {@link UsersResponse} containing the created admin user's details
     */
    @Transactional
    public UsersResponse create(CreateAdminRequest request) {

        Users users = usersMapper.createAdminRequestToUsers(request);
        String password = AppUtil.generatePassword(AppConstants.ADMIN_PASSWORD_LENGTH);
        users.setPassword(passwordEncoder.encode(password));
        users = usersRepository.save(users);
        usersRepository.saveRole(users.getId(), rolesRepository.findIdByName(Roles.ADMIN.getName()));

        log.info("Created admin user, password: ------> {}", password);
        // Notify the user about the password rest via mail
        MessageDetailsDto messageDetailsDto = MessageDetailsDto.builder().recipient(new String[]{users.getEmail()})
                .body("An account was created with your mail, kindly use this password to log in. Password is " + password)
                .subject(MessageSubjects.ADMIN_ACCOUNT_CREATED).build();
        MessageDto messageDto = MessageDto.builder().medium(MessageMedium.EMAIL).type(MessageType.ADMIN_ACCOUNT_CREATED).message(messageDetailsDto).build();
        kafkaSenderService.send(messageDto, Map.of(KafkaHeaders.TOPIC, KafkaTopics.ADMIN_ACCOUNT_CREATED));
        return usersMapper.usersToUserResponse(users);
    }

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
        Long userId = AppUtil.getLoggedInUserId();
        int updated = userProfileRepository.updateDob(userId, request.dob());
        requireNonNull(cacheManager.getCache(AppConstants.USERS_CACHE_NAME)).evict(AppUtil.getLoggedInUserEmail());
        return DobResponse.builder().status(updated > 0).message(updated > 0 ? "Dob successfully updated." : "Invalid id passed").build();
    }

    public GenderResponse updateGender(GenderRequest request) {
        Long userId = AppUtil.getLoggedInUserId();
        int updated = userProfileRepository.updateGender(userId, request.gender().name());
        requireNonNull(cacheManager.getCache(AppConstants.USERS_CACHE_NAME)).evict(AppUtil.getLoggedInUserEmail());
        return GenderResponse.builder().status(updated > 0).message(updated > 0 ? "Dob successfully updated." : "Invalid id passed").build();
    }
}
