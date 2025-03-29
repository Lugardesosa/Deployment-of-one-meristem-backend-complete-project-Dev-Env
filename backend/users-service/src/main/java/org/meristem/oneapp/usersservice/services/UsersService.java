package org.meristem.oneapp.usersservice.services;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.meristem.oneapp.usersservice.domains.requests.CreateUserRequest;
import org.meristem.oneapp.usersservice.domains.responses.UserResponse;
import org.meristem.oneapp.usersservice.exceptionHandler.exceptions.BadRequestException;
import org.meristem.oneapp.usersservice.exceptionHandler.exceptions.ResourceNotFoundException;
import org.meristem.oneapp.usersservice.mappers.UsersMapper;
import org.meristem.oneapp.usersservice.models.UserProfile;
import org.meristem.oneapp.usersservice.models.Users;
import org.meristem.oneapp.usersservice.repositories.UserProfileRepository;
import org.meristem.oneapp.usersservice.repositories.UsersRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsersService {

    private final UsersRepository usersRepository;
    private final UserProfileRepository profileRepository;

    private final UsersMapper usersMapper;

    @Transactional
    public Object createUser(@Valid CreateUserRequest userRequest) {

        if (usersRepository.existsByEmailOrPhoneNumber(userRequest.email(), userRequest.phoneNumber())) {
            throw new BadRequestException("Email or Phone number already exists.");
        }
        Users user = usersMapper.createUserRequestToUsers(userRequest);
        user = usersRepository.save(user);
        UserProfile profile = UserProfile.builder().userId(user.getId()).build();
        profileRepository.save(profile);
        return userRequest;
    }

    public UserResponse getUser(String email) {
        return usersRepository.findByEmail(email); //.orElseThrow(() -> new ResourceNotFoundException("User not found", "user", email));
    }
}
