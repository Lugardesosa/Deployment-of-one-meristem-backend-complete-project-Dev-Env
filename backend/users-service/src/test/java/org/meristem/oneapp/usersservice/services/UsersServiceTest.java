package org.meristem.oneapp.usersservice.services;

import net.datafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.meristem.oneapp.usersservice.domains.responses.BvnQueryResponse;
import org.meristem.oneapp.usersservice.utils.EncryptionUtil;
import org.meristem.oneapp.usersservice.constants.AppConstants;
import org.meristem.oneapp.usersservice.domains.requests.CreateUserRequest;
import org.meristem.oneapp.usersservice.domains.requests.SetPasswordRequest;
import org.meristem.oneapp.usersservice.domains.requests.UpdateEmailRequest;
import org.meristem.oneapp.usersservice.domains.responses.UpdateResponse;
import org.meristem.oneapp.usersservice.domains.responses.UsersResponse;
import org.meristem.oneapp.usersservice.exception.exceptions.BadRequestException;
import org.meristem.oneapp.usersservice.mappers.UsersMapping;
import org.meristem.oneapp.usersservice.models.Users;
import org.meristem.oneapp.usersservice.repositories.*;
import org.meristem.oneapp.usersservice.services.implementations.HuaweiService;
import org.meristem.oneapp.usersservice.services.implementations.KafkaSenderService;
import org.meristem.oneapp.usersservice.services.implementations.UsersService;
import org.meristem.oneapp.usersservice.utils.AppUtil;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UsersServiceTest {

    @InjectMocks
    private UsersService usersService;

    @Mock
    private UsersRepository usersRepository;
    @Mock
    private UsersMapping usersMapper = UsersMapping.INSTANCE;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private KafkaSenderService kafkaSenderService;
    @Mock
    private UserProfileRepository userProfileRepository;
    @Mock
    private FilesRepository filesRepository;

    @Mock
    IdCardRepository idCardRepository;
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
    @Mock
    private EncryptionUtil encryptionUtil;

    @Mock
    private CacheManager cacheManager;

    Faker faker = new Faker();

    private Cache getCache() {
        Cache cache = mock(Cache.class);

        when(cacheManager.getCache(AppConstants.SIGN_UP_CACHE_NAME)).thenReturn(cache);
        return cache;
    }

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void createUser() {
        CreateUserRequest request = CreateUserRequest.builder().email(faker.internet().emailAddress()).bvn(faker.regexify("[0-9]{11}"))
                .firstName(faker.name().firstName()).middleName(faker.name().nameWithMiddle()).lastName(faker.name().lastName())
                .phoneNumber(faker.regexify(AppConstants.PHONE_NG_REGEX_PATTERN)).build();

        when(encryptionUtil.encrypt(request.bvn())).thenReturn(request.bvn());
        Cache cache = getCache();

        BvnQueryResponse bvnQueryResponse = BvnQueryResponse.builder()
                .email(request.email()).firstName(request.firstName()).lastName(request.lastName())
                .phoneNumber(request.phoneNumber()).bvn(request.bvn())
                .build();

        doNothing().when(cache).put(request.email(), bvnQueryResponse);

        UpdateResponse response = usersService.create(request);
        assertEquals(true, response.success());
        assertEquals("Successful", response.message());
    }

    @Test
    void createUserEmailOrPhoneNumberAlreadyExist() {
        CreateUserRequest request = CreateUserRequest.builder().email(faker.internet().emailAddress()).bvn(faker.regexify("[0-9]{11}"))
                .firstName(faker.name().firstName()).middleName(faker.name().nameWithMiddle()).lastName(faker.name().lastName())
                .phoneNumber(faker.regexify(AppConstants.PHONE_NG_REGEX_PATTERN)).build();

        when(usersRepository.existsByEmailOrPhoneNumber(request.email(), request.phoneNumber())).thenReturn(true);

        assertThrowsExactly(BadRequestException.class, () -> usersService.create(request));
    }

    @Test
    void setPassword() {

        String password = faker.regexify(AppConstants.PASSWORD_REGEX_PATTERN);
        SetPasswordRequest request = SetPasswordRequest.builder().password(password).email(faker.internet().emailAddress())
                .confirmPassword(password).build();

        BvnQueryResponse bvnQueryResponse = BvnQueryResponse.builder().emailVerified(true)
                .email(request.email()).firstName(faker.name().firstName()).lastName(faker.name().lastName())
                .phoneNumber(faker.regexify(AppConstants.PHONE_NG_REGEX_PATTERN)).bvn(faker.regexify("[0-9]{11}"))
                .build();

        Cache cache = getCache();

        when(cache.get(request.email(), BvnQueryResponse.class)).thenReturn(bvnQueryResponse);


        Users users = Users.builder().firstName(bvnQueryResponse.getFirstName()).lastName(bvnQueryResponse.getLastName())
                .phoneNumber(bvnQueryResponse.getPhoneNumber()).id(1L)
                .email(request.email()).password(passwordEncoder.encode(password))
                .build();
        when(usersRepository.save(any())).thenReturn(users);

        doNothing().when(cache).evict(request.email());

        UpdateResponse response = usersService.setPassword(request);

        assertEquals(true, response.success());
        assertEquals("Password successfully set.", response.message());
    }

    @Test
    void setPasswordInitialSignUpNotFound() {

        Cache cache = getCache();

        String password = faker.regexify(AppConstants.PASSWORD_REGEX_PATTERN);
        SetPasswordRequest request = SetPasswordRequest.builder().password(password).email(faker.internet().emailAddress())
                .confirmPassword(password).build();

        when(cache.get(request.email(), BvnQueryResponse.class)).thenReturn(null);

        assertThrowsExactly(AccessDeniedException.class, () -> usersService.setPassword(request));
    }

    @Test
    void setPasswordEmailNotVerified() {

        Cache cache = getCache();

        String password = faker.regexify(AppConstants.PASSWORD_REGEX_PATTERN);
        SetPasswordRequest request = SetPasswordRequest.builder().password(password).email(faker.internet().emailAddress())
                .confirmPassword(password).build();

        BvnQueryResponse bvnQueryResponse = BvnQueryResponse.builder().emailVerified(false)
                .email(request.email()).firstName(faker.name().firstName()).lastName(faker.name().lastName())
                .phoneNumber(faker.regexify(AppConstants.PHONE_NG_REGEX_PATTERN)).bvn(faker.regexify("[0-9]{11}"))
                .build();

        when(cache.get(request.email(), BvnQueryResponse.class)).thenReturn(bvnQueryResponse);

        assertThrowsExactly(BadRequestException.class, () -> usersService.setPassword(request));
    }


    @Test
    void updateEmail() {

        Cache cache = getCache();
        UpdateEmailRequest request = UpdateEmailRequest.builder().newEmail(faker.internet().emailAddress()).oldEmail(faker.internet().emailAddress()).build();

        BvnQueryResponse bvnQueryResponse = BvnQueryResponse.builder().emailVerified(true)
                .email(request.oldEmail()).firstName(faker.name().firstName()).lastName(faker.name().lastName())
                .phoneNumber(faker.regexify(AppConstants.PHONE_NG_REGEX_PATTERN)).bvn(faker.regexify("[0-9]{11}"))
                .build();
        when(cache.get(request.oldEmail(), BvnQueryResponse.class)).thenReturn(bvnQueryResponse);

        doNothing().when(cache).put(request.newEmail(), bvnQueryResponse);
        doNothing().when(cache).evict(request.oldEmail());

        UpdateResponse response = usersService.updateEmail(request);
        assertEquals(true, response.success());
        assertEquals("Email successfully updated", response.message());

    }


    @Test
    void getUser() {

        try (MockedStatic<AppUtil> mockedStatic = mockStatic(AppUtil.class)) {
            mockedStatic.when(AppUtil::getLoggedInUserId).thenReturn(1L);
            when(usersRepository.findUserDetailsById(1L)).thenReturn(Optional.of(UsersResponse.builder().id(1L).userInstrumentResponses(List.of(UsersResponse.UserInstrumentResponse.builder().code("MER-TRADE").accessed(true).name("Trade").dataSharingAllowed(true).build())).build()));
            UsersResponse response = usersService.getUser();
            assertEquals(1L, response.id());
        }
    }

    @Test
    void getUserNotFound() {

        try (MockedStatic<AppUtil> mockedStatic = mockStatic(AppUtil.class)) {
            mockedStatic.when(AppUtil::getLoggedInUserId).thenReturn(1L);
            when(usersRepository.findUserDetailsById(1L)).thenThrow(new BadRequestException("User not found."));
            assertThrowsExactly(BadRequestException.class, () -> usersService.getUser());
        }
    }

    @Test
    void getUserJwtTokenNotFound() {

        try (MockedStatic<AppUtil> mockedStatic = mockStatic(AppUtil.class)) {
            mockedStatic.when(AppUtil::getLoggedInUserId).thenThrow(new BadRequestException("User is not logged in"));
            assertThrowsExactly(BadRequestException.class, () -> usersService.getUser());
        }
    }
}