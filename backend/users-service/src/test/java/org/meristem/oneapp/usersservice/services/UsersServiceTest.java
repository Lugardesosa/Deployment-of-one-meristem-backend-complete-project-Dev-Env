package org.meristem.oneapp.usersservice.services;

import net.datafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.meristem.oneapp.usersservice.constants.AppConstants;
import org.meristem.oneapp.usersservice.domains.enums.UserStatus;
import org.meristem.oneapp.usersservice.domains.requests.CreateUserRequest;
import org.meristem.oneapp.usersservice.domains.responses.UsersResponse;
import org.meristem.oneapp.usersservice.mappers.UsersMapping;
import org.meristem.oneapp.usersservice.models.Users;
import org.meristem.oneapp.usersservice.repositories.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.CacheManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class UsersServiceTest {

    @InjectMocks
    private UsersService usersService;

    @Mock
    private UsersRepository usersRepository;
    @Mock
    private UsersMapping usersMapper = UsersMapping.INSTANCE;
    @Mock
    private OtpVerificationRepository otpVerificationRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private KafkaSenderService kafkaSenderService;
    @Mock
    private UserProfileRepository userProfileRepository;
    @Mock
    private ImagesRepository imagesRepository;
    @Mock
    private CacheManager cacheManager;
    @Mock
    private UserProfileRepository profileRepository;
    @Mock
    private RequirementsRepository requirementsRepository;
    @Mock
    private UserOnboardingRepository userOnboardingRepository;
    @Mock
    private RolesRepository rolesRepository;
    @Mock
    private HuaweiService huaweiService;
    @Mock
    private GeneralRepository generalRepository;
    @Mock
    private CustomRepository customRepository;

    Faker faker = new Faker();

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void createUser() {
        CreateUserRequest request = CreateUserRequest.builder().email(faker.internet().emailAddress())
                .firstName(faker.name().firstName()).middleName(faker.name().nameWithMiddle()).lastName(faker.name().lastName())
                .phoneNumber(faker.regexify(AppConstants.PHONE_NG_REGEX_PATTERN)).build();
        Users users = Users.builder().id(1L).email(request.email())
                .phoneNumber(request.phoneNumber()).lastName(request.lastName()).firstName(request.firstName()).middleName(request.middleName()).build();
        users.setStatus(UserStatus.EMAIL_NOT_VERIFIED.getValue());
        given(usersRepository.save(any(Users.class))).willReturn(users);
        UsersResponse response = usersService.createUser(request);
        assertEquals(response.email(), request.email());
        assertEquals(response.firstName(), request.firstName());
        assertEquals(response.status(), UserStatus.EMAIL_NOT_VERIFIED.getValue());
        verify(usersRepository).save(any(Users.class));
    }
}