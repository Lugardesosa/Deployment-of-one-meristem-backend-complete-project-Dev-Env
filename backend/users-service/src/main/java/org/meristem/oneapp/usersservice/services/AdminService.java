package org.meristem.oneapp.usersservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.meristem.oneapp.kafka.dtos.AdminAccountDto;
import org.meristem.oneapp.kafka.dtos.MessageDto;
import org.meristem.oneapp.usersservice.constants.AppConstants;
import org.meristem.oneapp.usersservice.constants.KafkaTopics;
import org.meristem.oneapp.usersservice.constants.MessageSubjects;
import org.meristem.oneapp.usersservice.domains.enums.MessageMedium;
import org.meristem.oneapp.usersservice.domains.enums.MessageType;
import org.meristem.oneapp.usersservice.domains.enums.PermissionsEnum;
import org.meristem.oneapp.usersservice.domains.enums.UserStatus;
import org.meristem.oneapp.usersservice.domains.requests.*;
import org.meristem.oneapp.usersservice.domains.responses.*;
import org.meristem.oneapp.usersservice.exception.exceptions.BadRequestException;
import org.meristem.oneapp.usersservice.exception.exceptions.ResourceNotFoundException;
import org.meristem.oneapp.usersservice.mappers.UsersMapping;
import org.meristem.oneapp.usersservice.models.*;
import org.meristem.oneapp.usersservice.repositories.*;
import org.meristem.oneapp.usersservice.utils.AppUtil;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;


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
    private final UserProfileRepository userProfileRepository;
    private final UsersMapping usersMapper = UsersMapping.INSTANCE;
    private final KafkaSenderService kafkaSenderService;
    private final PasswordEncoder passwordEncoder;
    private final RolesRepository rolesRepository;
    private final CustomRepository customRepository;
    private final CacheManager cacheManager;
    private final PermissionsRepository permissionsRepository;
    private final AdminProfileRepository adminProfileRepository;
    private final InvestmentInstrumentsRepository investmentInstrumentsRepository;

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

    @CacheEvict(value = AppConstants.USERS_CACHE_NAME, key = "#request.userId")
    public DobResponse updateDob(DobRequest request) {
        int updated = userProfileRepository.updateDob(request.userId(), request.dob());
        return DobResponse.builder().status(updated > 0).message(updated > 0 ? "Dob successfully updated." : "Invalid id passed").build();
    }

    @CacheEvict(value = AppConstants.USERS_CACHE_NAME, key = "#request.userId")
    public GenderResponse updateGender(GenderRequest request) {
        int updated = userProfileRepository.updateGender(request.userId(), request.gender().name());
        return GenderResponse.builder().status(updated > 0).message(updated > 0 ? "Dob successfully updated." : "Invalid id passed").build();
    }


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
        Roles roles = rolesRepository.findById(request.roleId()).orElseThrow(() -> new BadRequestException("Role does not exist"));
        InvestmentInstruments investmentInstruments = investmentInstrumentsRepository.findById(request.investmentInstrumentId()).orElseThrow(() -> new BadRequestException("Subsidiary does not exist"));
        usersRepository.saveRole(users.getId(), roles.getId());
        customRepository.save(AdminProfile.builder().adminId(users.getId()).investmentInstrumentId(investmentInstruments.getId()).build());

        log.info("Created admin user, password: ------> {}", password);
        // Notify the user about the password rest via mail
        AdminAccountDto messageDetailsDto = AdminAccountDto.builder().recipient(new String[]{users.getEmail()})
                .body("An account was created with your mail, kindly use this password to log in. Password is " + password)
                .subject(MessageSubjects.ADMIN_ACCOUNT_CREATED).build();
        MessageDto messageDto = MessageDto.builder().medium(MessageMedium.EMAIL).isHtml(true).type(MessageType.ADMIN_ACCOUNT_CREATED).message(messageDetailsDto).classSimpleName(AdminAccountDto.class.getSimpleName()).build();
        kafkaSenderService.send(messageDto, Map.of(KafkaHeaders.TOPIC, KafkaTopics.ADMIN_ACCOUNT_CREATED, KafkaHeaders.KEY, users.getId()));
        return usersMapper.usersToUserResponse(users);
    }

    /**
     * Enables or disables admin user based on request
     */
    @Transactional
    public UpdateResponse enable(EnableAdminRequest request) {

        Users users = usersRepository.findById(request.userId()).orElseThrow(() -> new BadRequestException("User not found"));

        boolean isAdmin = rolesRepository.findAllByUsersId(request.userId()).stream().noneMatch(role -> role.getName().equals(AppConstants.USER_ROLE));

        if (!isAdmin) {
            throw new IllegalArgumentException("User is not an admin");
        }

        if (!request.enableAdmin()) {
            users.setStatus(UserStatus.DEACTIVATED.getValue());
        } else {
            users.setStatus(UserStatus.ACTIVE.getValue());
        }
        usersRepository.save(users);

        Objects.requireNonNull(cacheManager.getCache(AppConstants.USERS_CACHE_NAME)).evict(users.getEmail());

        return UpdateResponse.builder().success(true).message("Admin successfully " + (request.enableAdmin() ? "activated." : "deactivated.")).build();
    }

    @Transactional
    public UpdateResponse assignRole(AssignAdminRoleRequest request) {

        if (!usersRepository.existsById(request.userId())) {
            throw new ResourceNotFoundException("User not found", "User", request.userId().toString());
        }
        Roles roles = rolesRepository.findById(request.roleId()).orElseThrow(() -> new BadRequestException("Role not found"));
        boolean isAdmin = !roles.getName().equals(AppConstants.USER_ROLE);

        if (!isAdmin) {
            throw new IllegalArgumentException("Role is not an admin");
        }

        boolean userAlreadyHasRole = rolesRepository.findAllByUsersId(request.userId()).stream().anyMatch(role -> roles.getName().equals(role.getName()));
        if (userAlreadyHasRole) {
            throw new BadRequestException("User already has this role");
        }

        int updated = rolesRepository.updateUserRole(request.userId(), request.roleId());
        return UpdateResponse.builder().success(updated > 0).message(updated + " role successfully assigned").build();
    }

    @Transactional
    public UpdateResponse assignPermission(AssignAdminPermissionRequest request) {

        if (!usersRepository.existsById(request.userId())) {
            throw new BadRequestException("User not found");
        }

        Permissions permissions = permissionsRepository.findById(request.permissionId()).orElseThrow(() -> new BadRequestException("Permission not found"));
        List<Roles> usersRoles = rolesRepository.findAllByUsersId(request.userId());
        List<String> rolesPermissions = permissionsRepository.findAllByRolesIds(usersRoles.stream().map(Roles::getId).collect(Collectors.toList()));
        rolesPermissions.addAll(permissionsRepository.findAllByUsersId(request.userId()));

        boolean userAlreadyHasPermission = rolesPermissions.contains(permissions.getName());
        if (userAlreadyHasPermission) {
            throw new BadRequestException("User already has this permission");
        }

        int updated = permissionsRepository.updateUserPermission(request.userId(), request.permissionId());
        return UpdateResponse.builder().success(updated > 0).message(updated + " permission successfully assigned").build();
    }

    public UpdateResponse addRole(AddRoleRequest request) {

        List<String> roleNames = rolesRepository.findAllNames();

        if (roleNames.contains(request.roleName())) {
            throw new BadRequestException("Role already exists");
        }

        Roles roles = Roles.builder().name(request.roleName()).build();
        rolesRepository.save(roles);
        return UpdateResponse.builder().success(true).message("Role successfully added").build();
    }

    public UpdateResponse addPermission(AddPermissionRequest request) {

        List<String> permissionNames = permissionsRepository.findAllNames();

        if (permissionNames.contains(request.permissionName())) {
            throw new BadRequestException("Permission already exists");
        }

        Optional<PermissionsEnum> permissionsEnum;
        String roleName;

        roleName = rolesRepository.findAllNames(request.roleId()).orElseThrow(() -> new BadRequestException("Role not found"));
        permissionsEnum = Arrays.stream(PermissionsEnum.values()).filter(p -> p.name().equals(roleName)).findFirst();

        Permissions next = permissionsRepository.findTopByNameStartsWithOrderByCodeDesc(permissionsEnum.orElseThrow().getStartsWith());

        Permissions permissions = Permissions.builder().name(permissionsEnum.get().getStartsWith().concat(".").concat(request.permissionName())).code(nonNull(next) ? Integer.parseInt(next.getCode()) + 1 + "" : permissionsEnum.get().getCode()).build();
        permissionsRepository.save(permissions);
        return UpdateResponse.builder().success(true).message("Permission successfully added").build();
    }

    public UpdateResponse addPermissionToRole(AddPermissionToRoleRequest request) {

        if (!rolesRepository.existsById(request.roleId())) {
            throw new BadRequestException("Role not found");
        }

        if (!permissionsRepository.existsById(request.permissionId())) {
            throw new BadRequestException("Permission not found");
        }

        if (permissionsRepository.existsByRolesIdAndPermissionsId(request.roleId(), request.permissionId())) {
            throw new BadRequestException("Permission already assigned to role");
        }

        int updated = permissionsRepository.updateRolePermission(request.roleId(), request.permissionId());
        return UpdateResponse.builder().success(updated > 0).message(updated + " permission successfully assigned").build();
    }

    public RolesResponse getRoles(Long userId) {

        if (nonNull(userId)) {
            return new RolesResponse(rolesRepository.findAllRoles(userId));
        }
        return new RolesResponse(rolesRepository.findAllRoles());
    }

    public PermissionsResponse getPermissions(Long userId, Long roleId) {

        if (nonNull(userId)) {
            List<Long> roleIds = rolesRepository.findAllRolesId(userId);
            return new PermissionsResponse(permissionsRepository.findAllPermissionsByUserIdAndRoleIds(userId, roleIds.isEmpty() ? null : roleIds));
        }

        if (nonNull(roleId)) {
            return new PermissionsResponse(permissionsRepository.findAllPermissionsByRoles(roleId));
        }
        return new PermissionsResponse(permissionsRepository.findAllPermissions());
    }

    public AdminsResponse getAdmins(Long adminId) {

        List<Long> adminIds;
        if (nonNull(adminId)) {
            adminIds = Collections.singletonList(adminId);
        } else {
            adminIds = adminProfileRepository.findAllAdminIds();
        }

        return new AdminsResponse(usersRepository.findAllAdminsByIds(adminIds));
    }

    @Transactional
    public UpdateResponse updateAdmin(UpdateAdminRequest request) {

        usersRepository.findById(request.adminId()).orElseThrow(() -> new BadRequestException("Admin does not exist."));
        InvestmentInstruments investmentInstruments = investmentInstrumentsRepository.findById(request.subsidiaryId()).orElseThrow(() -> new BadRequestException("Subsidiary does not exist"));

        int updated = adminProfileRepository.updateAdminProfile(investmentInstruments.getId());

        rolesRepository.findByIdAndNameIsNotLike(request.roleId(), org.meristem.oneapp.usersservice.domains.enums.Roles.USER.name())
                .orElseThrow(() -> new BadRequestException("Role does not exist or Role is not an admin."));

        rolesRepository.deleteUserRole(request.adminId());
        int updatedRole = rolesRepository.updateUserRole(request.adminId(), request.roleId());
        return UpdateResponse.builder().success(updated > 0 || updatedRole > 0).message(updatedRole + " role and " + updated + " subsidiary successfully updated").build();
    }
}
