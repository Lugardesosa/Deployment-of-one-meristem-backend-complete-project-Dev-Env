package org.meristem.oneapp.usersservice.services.implementations;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.meristem.oneapp.usersservice.domains.enums.IdCardType;
import org.meristem.oneapp.usersservice.domains.requests.IdQueryRequest;
import org.meristem.oneapp.usersservice.domains.responses.BvnQueryResponse;
import org.meristem.oneapp.usersservice.domains.responses.NinValidationResponse;
import org.meristem.oneapp.usersservice.dtos.IdQueryDetailsDto;
import org.meristem.oneapp.usersservice.exception.exceptions.BadRequestException;
import org.meristem.oneapp.usersservice.integrations.DojahClient;
import org.meristem.oneapp.usersservice.integrations.responses.DojahBvnLookUpResponse;
import org.meristem.oneapp.usersservice.integrations.responses.DojahNinLookUpResponse;
import org.meristem.oneapp.usersservice.mappers.UserIdDetailsMapper;
import org.meristem.oneapp.usersservice.models.UserIdDetails;
import org.meristem.oneapp.usersservice.models.Users;
import org.meristem.oneapp.usersservice.repositories.*;
import org.meristem.oneapp.usersservice.services.IIdDetailsService;
import org.meristem.oneapp.usersservice.services.IKycService;
import org.meristem.oneapp.usersservice.services.IUsersService;
import org.meristem.oneapp.usersservice.utils.AppUtil;
import org.meristem.oneapp.usersservice.utils.EncryptionUtil;
import org.meristem.oneapp.usersservice.utils.HashingUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


/**
 * Service class for handling Smile ID-related operations, including generating Smile ID links,
 * processing webhook notifications, and managing user onboarding requirements.
 *
 * <p>This class integrates with Smile ID's API to create smart links for user verification,
 * handle webhook notifications for verification results, and update user records accordingly.</p>
 *
 * <p>Annotated with {@link Service} to indicate that it is a Spring-managed service component.
 * Transactional methods ensure atomicity for database operations.</p>
 *
 * <p>Dependencies are injected via constructor injection, and the class uses {@link Slf4j}
 * for logging purposes.</p>
 */
@Slf4j
@RequiredArgsConstructor
@Service("DOJAH")
@Transactional
public class DojahService implements IKycService {

    private final CustomRepository customRepository;
    private final UserIdDetailsMapper userIdDetailsMapper = UserIdDetailsMapper.INSTANCE;
    @Value("${hashing.id-hash-key}")
    private String idHashKey;

    private final UserOnboardingRepository userOnboardingRepository;
    private final RequirementsRepository requirementsRepository;
    private final UsersRepository usersRepository;
    private final CacheManager cacheManager;
    private final IdCardRepository idCardRepository;
    private final IUsersService usersService;
    private final HashingUtil hashingUtil;
    private final EncryptionUtil encryptionUtil;
    private final IIdDetailsService idDetailsService;

    private final DojahClient dojahClient;
    public BvnQueryResponse bvnQuery(IdQueryRequest request) {

        if (IdCardType.BVN.compareTo(IdCardType.fromName(request.idType())) != 0) {
            throw new BadRequestException("Only BVN can be validated.");
        }

        if (idCardRepository.existsByIdValueHashedAndIdCardType(hashingUtil.hmacWithSha256(idHashKey, request.idNumber()), IdCardType.BVN.getName())) {
            throw new BadRequestException("BVN already exists.");
        }
        DojahBvnLookUpResponse response = dojahClient.dojahBvnLookUp(request.idNumber());

        IdQueryDetailsDto dto = userIdDetailsMapper.dojahBvnLookupResponseToIdQueryDetailsDto(response.entity());
        dto.setIdType(IdCardType.BVN.getName());

        return getBvnQueryResponse(cacheManager, request, dto, encryptionUtil, hashingUtil, idHashKey);
    }

    /**
     * Validates NIN data; flags mismatches; completes onboarding if valid
     */
    public NinValidationResponse validateNin(IdQueryRequest request) {

        if (IdCardType.NIN.compareTo(IdCardType.fromName(request.idType())) != 0) {
            throw new BadRequestException("Only NIN can be validated.");
        }

        if (idCardRepository.existsByIdValueHashedAndIdCardType(hashingUtil.hmacWithSha256(idHashKey, request.idNumber()), IdCardType.NIN.getName())) {
            throw new BadRequestException("NIN already exists.");
        }

        DojahNinLookUpResponse response = dojahClient.dojahNinLookUpAdvance(request.idNumber());

        IdQueryDetailsDto dto = userIdDetailsMapper.dojahNinLookupResponseToIdQueryDetailsDto(response.entity());
        dto.setIdType(IdCardType.NIN.getName());
        Users loggedInUser = usersRepository.findById(AppUtil.getLoggedInUserId()).orElseThrow(() -> new AuthorizationDeniedException("User is not logged in"));
        UserIdDetails bvn = customRepository.findOneBy(UserIdDetails.class, Map.of("userId", loggedInUser.getId(), "idType", IdCardType.BVN.getName())).orElseThrow(() -> new BadRequestException("BVN details could not be found."));

        List<String> names = buildNames(bvn);

        UserIdDetails nin = idDetailsService.buildAndSaveIdDetails(dto, loggedInUser);
        return compareNinAndBvnDetailsSaveAndReturn(nin, names, bvn, loggedInUser, requirementsRepository, userOnboardingRepository, usersService, customRepository);
    }
}
