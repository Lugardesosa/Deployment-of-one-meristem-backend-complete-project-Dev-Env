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
import org.meristem.oneapp.usersservice.domains.responses.UsersResponse;
import org.meristem.oneapp.usersservice.mappers.UsersMapping;
import org.meristem.oneapp.usersservice.models.Users;
import org.meristem.oneapp.usersservice.repositories.RolesRepository;
import org.meristem.oneapp.usersservice.repositories.UsersRepository;
import org.meristem.oneapp.usersservice.utils.AppUtil;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class SuperAdminService {

    private final UsersRepository usersRepository;
    private final UsersMapping usersMapper = UsersMapping.INSTANCE;
    private final KafkaSenderService kafkaSenderService;
    private final PasswordEncoder passwordEncoder;
    private final RolesRepository rolesRepository;

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
        MessageDto messageDto = MessageDto.builder().medium(MessageMedium.EMAIL).type(MessageType.ADMIN_ACCOUNT_CREATED).message(messageDetailsDto).classSimpleName(MessageDetailsDto.class.getSimpleName()).build();
        kafkaSenderService.send(messageDto, Map.of(KafkaHeaders.TOPIC, KafkaTopics.ADMIN_ACCOUNT_CREATED));
        return usersMapper.usersToUserResponse(users);
    }
}
