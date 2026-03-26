package org.meristem.oneapp.usersservice.services.implementations;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.meristem.oneapp.usersservice.constants.AppConstants;
import org.meristem.oneapp.usersservice.domains.requests.FacialVerificationRequest;
import org.meristem.oneapp.usersservice.domains.requests.IdQueryRequest;
import org.meristem.oneapp.usersservice.domains.requests.NinValidationRequest;
import org.meristem.oneapp.usersservice.domains.responses.BvnQueryResponse;
import org.meristem.oneapp.usersservice.domains.responses.IdValidationResponse;
import org.meristem.oneapp.usersservice.domains.responses.UpdateResponse;
import org.meristem.oneapp.usersservice.dtos.IdQueryDetailsDto;
import org.meristem.oneapp.usersservice.exception.exceptions.ResourceNotFoundException;
import org.meristem.oneapp.usersservice.services.IKycDelegatingService;
import org.meristem.oneapp.usersservice.services.IKycService;
import org.meristem.oneapp.usersservice.utils.HashingUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import static java.util.Objects.requireNonNull;


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
@Service
@Transactional
public class KycDelegatingService implements IKycDelegatingService {

    private final IKycService smileIdService;
    private final IKycService dojahService;
    private final RedisCacheManager cacheManager;
    private final HashingUtil hashingUtil;
    @Value("${hashing.id-hash-key}")
    private String idHashKey;

    @Value("${spring.profiles.active}")
    private String activeProfile;

    public KycDelegatingService(@Qualifier("SMILE_ID") IKycService smileIdService, @Qualifier("DOJAH") IKycService dojahService, RedisCacheManager cacheManager, HashingUtil hashingUtil) {
        this.smileIdService = smileIdService;
        this.dojahService = dojahService;
        this.cacheManager = cacheManager;
        this.hashingUtil = hashingUtil;
    }

    @Override
    // TODO: UNCOMMENT TO ENABLE DOJAH
//    @CircuitBreaker(name = "dojah", fallbackMethod = "smileIdBvnQuery")
    public BvnQueryResponse bvnQuery(IdQueryRequest request) {
        return smileIdService.bvnQuery(request);
    }

    // TODO: UNCOMMENT TO ENABLE DOJAH
//    public BvnQueryResponse smileIdBvnQuery(IdQueryRequest request, Throwable throwable) {
//
//        if (throwable instanceof ResourceNotFoundException ex && "prod".equalsIgnoreCase(activeProfile)) {
//            throw new ResourceNotFoundException("ID query not found", request.idType(), request.idNumber());
//        }
//        return dojahService.bvnQuery(request);
//    }

    @Override
    // TODO: UNCOMMENT TO ENABLE DOJAH
//    @CircuitBreaker(name = "dojah", fallbackMethod = "smileIdValidateNin")
    public IdValidationResponse validateNin(NinValidationRequest request) {
        return this.smileIdService.validateNin(request);
    }

    // TODO: UNCOMMENT TO ENABLE DOJAH
//    public IdValidationResponse smileIdValidateNin(NinValidationRequest request, Throwable throwable) {
//        if (throwable instanceof ResourceNotFoundException ex && "prod".equalsIgnoreCase(activeProfile)) {
//            throw new ResourceNotFoundException("ID query not found", request.idType(), request.idNumber());
//        }
//        return this.dojahService.validateNin(request);
//    }


    // TODO: UNCOMMENT TO ENABLE DOJAH
//    @CircuitBreaker(name = "dojah", fallbackMethod = "smileIdNinQuery")
    @Override
    public IdQueryDetailsDto ninQuery(String nin) {
        return this.smileIdService.ninQuery(nin);
    }

    // TODO: UNCOMMENT TO ENABLE DOJAH
//    public IdQueryDetailsDto smileIdNinQuery(String nin, Throwable throwable) {
//
//        if (throwable instanceof ResourceNotFoundException ex && "prod".equalsIgnoreCase(activeProfile)) {
//            throw new ResourceNotFoundException("ID query not found", "NIN", nin);
//        }
//        return dojahService.ninQuery(nin);
//    }

    @Override
    public IdValidationResponse validateBvn(MultipartFile file) {
        return dojahService.bvnValidation(file);
    }


    @Override
    public UpdateResponse facialVerification(MultipartFile file, FacialVerificationRequest request) {

        Cache cache = requireNonNull(cacheManager.getCache(AppConstants.SIGN_UP_CACHE_NAME));
        IdQueryDetailsDto bvnQueryResponse = cache.get(hashingUtil.hmacWithSha256(idHashKey, request.idNumber()), IdQueryDetailsDto.class);

        if (bvnQueryResponse == null) {
            throw new ResourceNotFoundException("Initial sign up details not found.", "Bvn", request.idNumber().substring(0, 3) + "*****" + request.idNumber().substring(8, 11));
        }

        // TODO: apply facial verification

        bvnQueryResponse.setBvnFacialVerified(true);
        cache.put(bvnQueryResponse.getBvnHashed(), bvnQueryResponse);
        return UpdateResponse.builder().success(true).message(bvnQueryResponse.getEmail() + ":" + bvnQueryResponse.getPhoneNumber()).build();
    }
}
